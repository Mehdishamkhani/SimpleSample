/*
 * @author Mehdi Shamkhani  2019
 * https://github.com/mehdishamkhani
 *
 */

package org.sana.simpleapp


import android.app.Activity
import android.support.multidex.MultiDexApplication

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.sanasimpleapp.R

import java.security.Security

import javax.inject.Inject

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.sana.simpleapp.di.component.DaggerAppComponent


class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>


    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }


    override fun onCreate() {
        super.onCreate()

        /*
         * pump view customization in whole of the app
         */

        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/iran.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build())).build())


        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

    }






}
