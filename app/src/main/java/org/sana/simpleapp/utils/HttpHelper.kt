package org.sana.simpleapp.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast

import org.sanasimpleapp.R

import retrofit2.HttpException

/**
 * Created by mehdi on 19/10/2019.
 */

object HttpHelper {

    const val HTTP_ERROR_AUTH = 401
    const val HTTP_OK = 200
    const val HTTP_ERROR_PAYMENT = 402
    const val HTTP_ERROR_INTERNAL = 500
    const val HTTP_ERROR_NOTFOUND = 404
    const val HTTP_CREATED = 201
    const val HTTP_ERROR_UNPROCESSABLE = 422
    const val HTTP_ERROR_NOT_ACCEPTABLE = 406
    const val DEFAULT_CODE = -1

    fun showError(context: Context?, t: Throwable?): Int {

        if (t == null || context == null) return DEFAULT_CODE

        if (t is HttpException) {
            if (context is Activity) {
                context.runOnUiThread { Toast.makeText(context, makeMessage(context, t.code()), Toast.LENGTH_LONG).show() }
            }

            return t.code()
        }

        return DEFAULT_CODE
    }

    fun showError(activity: Activity?, message: String?) {

        if (message == null || activity == null) return

        activity.runOnUiThread { Toast.makeText(activity, message, Toast.LENGTH_LONG).show() }

    }

    private fun makeMessage(context: Context?, code: Int): String {

        when (code) {

            HTTP_ERROR_NOTFOUND -> return "آدرس یافت نشد"

            HTTP_ERROR_INTERNAL -> return "خطای داخلی سرور"
        }

        return context!!.getString(R.string.general_err)
    }

}
