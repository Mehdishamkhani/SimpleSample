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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mCenterLatLong: LatLng? = null

    internal var mLastLocation: Location? = null


    private var mResultReceiver: AddressResultReceiver? = null
    protected var mAddressOutput: String? = null
    protected var mAreaOutput: String? = null
    protected var mCityOutput: String? = null
    protected var mStateOutput: String? = null


    internal var mLocationCallback = LocationCallback()
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)

        findViewById<View>(R.id.choose_location).setOnClickListener {
            val toRegisterActivity = Intent(this@MapsActivity, RegisterActivity::class.java)
            toRegisterActivity.putExtra("lat", mCenterLatLong!!.latitude)
            toRegisterActivity.putExtra("long", mCenterLatLong!!.longitude)
            startActivity(toRegisterActivity)
        }


        mapFragment.getMapAsync(this)
        mResultReceiver = AddressResultReceiver(Handler())

        if (checkPlayServices()) {

            if (!AppUtils.isLocationEnabled(this@MapsActivity)) {

                val dialog = AlertDialog.Builder(this@MapsActivity)
                dialog.setMessage("Location not enabled!")
                dialog.setPositiveButton("Open location settings") { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }

                dialog.setNegativeButton("Cancel") { paramDialogInterface, paramInt ->
                    paramDialogInterface?.dismiss()
                }
                dialog.show()
            }

            buildGoogleApiClient()

        } else {
            Toast.makeText(this@MapsActivity, "Location not supported in this device", Toast.LENGTH_SHORT).show()
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

            try {

                val mLocation = Location("")
                mLocation.latitude = mCenterLatLong!!.latitude
                mLocation.longitude = mCenterLatLong!!.longitude

                startIntentService(mLocation)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }


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
    protected fun buildGoogleApiClient() {
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
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show()
            }
            return false
        }
        return true
    }

    private fun changeMap(location: Location?) {

        Log.d(TAG, "Reaching map" + mMap!!)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if (mMap != null) {
            mMap!!.uiSettings.isZoomControlsEnabled = false
            val latLong = LatLng(location!!.latitude, location.longitude)

            val cameraPosition = CameraPosition.Builder()
                    .target(latLong).zoom(19f).tilt(70f).build()

            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition))

            startIntentService(location)


        } else {
            Toast.makeText(applicationContext,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show()
            finish()
        }

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY)
            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA)
            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY)
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET)


            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {

            }

        }

    }


    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected fun startIntentService(mLocation: Location) {


        if (!Geocoder.isPresent()) {
            Toast.makeText(this@MapsActivity,
                    "geo coder not available",
                    Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, FetchAddressIntentService::class.java)
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver)
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation)
        startService(intent)
    }

    companion object {
        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private val TAG = "MAP LOCATION"
    }


}