package org.sana.simpleapp.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import com.novoda.merlin.*
import dagger.android.AndroidInjection
import io.github.inflationx.viewpump.ViewPumpContextWrapper


/**
 * Created by mehdi on 19/10/2019.
 */

open class BaseActivity : MerlinActivity(), Connectable, Disconnectable, Bindable {

    override fun createMerlin(): Merlin {
        return Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(this)
    }

    override fun onConnect() {
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        AndroidInjection.inject(this)

    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        registerConnectable(this);
        registerDisconnectable(this);
        registerBindable(this);
    }


    override fun onDisconnect() {

        //SUPER SIMPLE APPROACH!! :)
        val dialog = AlertDialog.Builder(this@BaseActivity)
        dialog.setMessage(getString(org.sanasimpleapp.R.string.internet_disconnected))
        dialog.setNegativeButton(getString(org.sanasimpleapp.R.string.ok)) { paramDialogInterface, paramInt ->
            paramDialogInterface?.dismiss()
        }
        dialog.show()


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBind(networkStatus: NetworkStatus) {
        if (!networkStatus.isAvailable) {
            onDisconnect()
        }
    }

}
