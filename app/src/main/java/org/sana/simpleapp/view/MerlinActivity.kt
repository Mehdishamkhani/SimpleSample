package org.sana.simpleapp.view

import android.os.Bundle
import android.util.Log

import com.novoda.merlin.Bindable
import com.novoda.merlin.Connectable
import com.novoda.merlin.Disconnectable
import com.novoda.merlin.Logger
import com.novoda.merlin.Merlin

import androidx.appcompat.app.AppCompatActivity

abstract class MerlinActivity : AppCompatActivity() {

    private var logHandle: DemoLogHandle? = null
    protected var merlin: Merlin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logHandle = DemoLogHandle()
        merlin = createMerlin()
    }

    protected abstract fun createMerlin(): Merlin

    protected fun registerConnectable(connectable: Connectable) {
        merlin.registerConnectable(connectable)
    }

    protected fun registerDisconnectable(disconnectable: Disconnectable) {
        merlin.registerDisconnectable(disconnectable)
    }

    protected fun registerBindable(bindable: Bindable) {
        merlin.registerBindable(bindable)
    }

    override fun onStart() {
        super.onStart()
        Logger.attach(logHandle)
        merlin.bind()
    }

    override fun onStop() {
        super.onStop()
        merlin.unbind()
        Logger.detach(logHandle)
    }

    private class DemoLogHandle : Logger.LogHandle {

        override fun v(vararg message: Any) {
            Log.v(TAG, message[0].toString())
        }

        override fun i(vararg message: Any) {
            Log.i(TAG, message[0].toString())
        }

        override fun d(vararg msg: Any) {
            Log.d(TAG, msg[0].toString())
        }

        override fun d(throwable: Throwable, vararg message: Any) {
            Log.d(TAG, message[0].toString(), throwable)
        }

        override fun w(vararg message: Any) {
            Log.w(TAG, message[0].toString())
        }

        override fun w(throwable: Throwable, vararg message: Any) {
            Log.w(TAG, message[0].toString(), throwable)
        }

        override fun e(vararg message: Any) {
            Log.e(TAG, message[0].toString())
        }

        override fun e(throwable: Throwable, vararg message: Any) {
            Log.e(TAG, message[0].toString(), throwable)
        }

        companion object {

            private val TAG = "DemoLogHandle"
        }
    }

}