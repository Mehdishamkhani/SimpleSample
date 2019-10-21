package org.sana.simpleapp.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import org.sana.simpleapp.FetchAddressIntentService
import org.sana.simpleapp.utils.AppUtils
import org.sanasimpleapp.R

/**
 * Created by mehdi on 19/10/2019.
 */

class MapActivity : BaseActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mCenterLatLong: LatLng? = null

    internal var mLastLocation: Location? = null


    private var mResultReceiver: AddressResultReceiver? = null
    protected var mAddressOutput: String? = ""


    internal var mLocationCallback = LocationCallback()
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        checkGooglePlayIsAvailable()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapActivity)
        mapFragment.getMapAsync(this)
        mResultReceiver = AddressResultReceiver(Handler())

        findViewById<View>(R.id.choose_location).setOnClickListener {
            val toRegisterActivity = Intent(this@MapActivity, RegisterActivity::class.java)
            toRegisterActivity.putExtra(AppUtils.LAT_TAG, mCenterLatLong?.latitude)
            toRegisterActivity.putExtra(AppUtils.LNG_TAG, mCenterLatLong?.longitude)
            toRegisterActivity.putExtra(AppUtils.ADDRESS_TAG, mAddressOutput)
            startActivity(toRegisterActivity)
        }

        if (!hasPermission())
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE);

    }


    //SUPER SIMPLE APPROACH!
    private fun hasPermission(): Boolean {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            else {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            }
        }

    }


    private fun checkGooglePlayIsAvailable() {
        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(this@MapActivity)) {
                val dialog = AlertDialog.Builder(this@MapActivity)
                dialog.setMessage(getString(R.string.location_not_enabled))
                dialog.setPositiveButton(getString(R.string.location_setting)) { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                dialog.setNegativeButton(getString(R.string.cancel)) { paramDialogInterface, paramInt ->
                    paramDialogInterface?.dismiss()
                }
                dialog.show()
            }
            buildGoogleApiClient()
        } else {
            Toast.makeText(this@MapActivity, "Location not supported in this device", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {

        Log.d(TAG, "OnMapReady")

        mMap = googleMap
        mMap!!.setOnCameraMoveStartedListener(GoogleMap.OnCameraMoveStartedListener {
            if (mMap == null) {
                return@OnCameraMoveStartedListener
            }

            mMap!!.clear()
            mCenterLatLong = mMap!!.cameraPosition.target

            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(35.6892, 51.3890)).zoom(19f).build()
            mMap!!.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition))

            try {

                val mLocation = Location("")
                mLocation.latitude = mCenterLatLong!!.latitude
                mLocation.longitude = mCenterLatLong!!.longitude

                startIntentService(mLocation)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        if (!hasPermission())
            return

    }

    override fun onConnected(bundle: Bundle?) {

        if (!hasPermission())
            return


        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                mLastLocation = location
                changeMap(mLastLocation)
                Log.d(TAG, "ON connected")

            } else {

                fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)

                val mLocationRequest = LocationRequest()
                mLocationRequest.interval = 10000
                mLocationRequest.fastestInterval = 5000
                mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                fusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())

            }
        }


    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location?) {

        if (location != null) {
            changeMap(location)
        }

        fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }


    override fun onStart() {
        super.onStart()
        try {
            mGoogleApiClient!!.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance();
        val result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    private fun changeMap(location: Location?) {

        Log.d(TAG, "Reaching map" + mMap!!)


        if (!hasPermission())
            return;

        if (mMap != null) {

            mMap!!.uiSettings.isZoomControlsEnabled = false
            val latLong = LatLng(location!!.latitude, location.longitude)
            val cameraPosition = CameraPosition.Builder()
                    .target(latLong).zoom(19f).build()

            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition))

            startIntentService(location)

        } else {
            Toast.makeText(applicationContext, getString(R.string.map_loading_failed), Toast.LENGTH_SHORT).show()
            finish()
        }

    }


    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY)
        }
    }


    protected fun startIntentService(mLocation: Location) {

        if (!Geocoder.isPresent()) {
            return
        }

        val intent = Intent(this, FetchAddressIntentService::class.java)
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver)
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation)
        startService(intent)
    }

    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private const val TAG = "MAP LOCATION"
        const val REQUEST_CODE = 1213
    }


}