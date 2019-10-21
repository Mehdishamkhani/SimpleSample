package org.sana.simpleapp


import android.app.Activity
import android.support.multidex.MultiDexApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.sana.simpleapp.di.component.DaggerAppComponent
import org.sanasimpleapp.R
import javax.inject.Inject

/**
 * Created by mehdi on 19/10/2019.
 */


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


        /*
        * Dagger init
        */

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

    }






}
