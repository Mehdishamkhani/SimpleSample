package org.sana.simpleapp.view

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle

import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import io.github.inflationx.viewpump.ViewPumpContextWrapper

/**
 * Created by mehdi on 19/10/2019.
 */

open class DaggerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        AndroidInjection.inject(this)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

}
