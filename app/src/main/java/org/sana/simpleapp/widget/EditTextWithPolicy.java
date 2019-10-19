package org.sana.simpleapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import org.sana.simpleapp.utils.ValidationListener;
import org.sanasimpleapp.R;

/**
 * Created by mehdi on 19/10/2019.
 */

public class EditTextWithPolicy extends EditText implements TextWatcher {
    private String policy = "";
    private boolean isValid = false;
    private ValidationListener validationListener;
    private String message = "";

    public EditTextWithPolicy(Context context) {
        super(context);
    }


    public EditTextWithPolicy(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.EditTextWithPolicy);


        policy = a.getString(R.styleable.EditTextWithPolicy_policy);
        if (policy == null) policy = "general";

        addTextChangedListener(this);

        //Don't forget this
        a.recycle();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {


        switch (policy) {
            case "phone":
                isValid = true;
                message = "";
                break;

            case "address":
                isValid = true;
                message = "";

                break;

            case "name":
                isValid = true;
                message = "";

                break;

            default:
                isValid = true;
                message = "";

        }

        if (validationListener != null)
            validationListener.onValidationStatusChange(this, isValid);
    }


    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {

        Log.d("@@@",message);


        return message;
    }


    public void setValidationListener(ValidationListener listener) {

        this.validationListener = listener;
    }
}
