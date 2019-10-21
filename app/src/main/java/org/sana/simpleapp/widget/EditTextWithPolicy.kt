package org.sana.simpleapp.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import org.sana.simpleapp.widget.PolicyValidator.Companion.GENERAL_POLICY
import org.sanasimpleapp.R

/**
 * Created by mehdi on 19/10/2019.
 */


class EditTextWithPolicy : AppCompatEditText, TextWatcher, View.OnFocusChangeListener {
    private var policyValidator: PolicyValidator? = null
    private var policy: String? = null
    var isValid = false
        private set
    private var validationListener: ValidationListener? = null
    private var message = ""

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = getContext()
                .obtainStyledAttributes(attrs, R.styleable.EditTextWithPolicy)


        policy = a.getString(R.styleable.EditTextWithPolicy_policy)
        if (policy == null) {
            policy = GENERAL_POLICY
        }

        onFocusChangeListener = this
        policyValidator = PolicyValidator.init()
        message = policyValidator!!.message

        addTextChangedListener(this)
        a.recycle()
    }


    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }


    override fun afterTextChanged(editable: Editable) {

        val input = editable.toString()
        policyValidator = policyValidator!!.validate(policy, input)
        message = policyValidator!!.message
        isValid = policyValidator!!.isValid

        if (validationListener != null)
            validationListener!!.onValidationStatusChange(this, isValid)
    }

    fun getMessage(): String {

        Log.d(TAG, message)
        return message
    }


    fun setValidationListener(listener: ValidationListener) {
        this.validationListener = listener
    }


    override fun onFocusChange(view: View, hasFocus: Boolean) {

        if (parent is LinearLayout) {
            val parent = parent as LinearLayout
            val drawable: Drawable? = if (hasFocus)
                ContextCompat.getDrawable(context, R.drawable.field_bg_focused)
            else
                ContextCompat.getDrawable(context, R.drawable.field_bg_normal)
            parent.background = drawable
        }
    }

    companion object {
        private const val TAG = "@EditTextWithPolicy"
    }
}
