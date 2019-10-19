package org.sana.simpleapp.view

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import kotlinx.android.synthetic.main.activity_register.*
import org.sana.simpleapp.model.RegisterUserModel
import org.sana.simpleapp.utils.ValidatorComposite
import org.sana.simpleapp.utils.ValidationListener
import org.sana.simpleapp.viewmodel.UserViewModel
import org.sana.simpleapp.viewmodel.ViewModelFactory
import org.sana.simpleapp.widget.EditTextWithPolicy
import org.sanasimpleapp.R
import javax.inject.Inject


class RegisterActivity : DaggerActivity(), ValidationListener {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private lateinit var validatorComposite: ValidatorComposite<EditTextWithPolicy>



    override fun onValidationStatusChange(editTextWithPolicy: EditTextWithPolicy, isValid: Boolean) {


        val drawable: Drawable? = if (isValid)
            ContextCompat.getDrawable(this, R.drawable.googleg_disabled_color_18)
        else
            ContextCompat.getDrawable(this, R.drawable.googleg_standard_color_18)

        editTextWithPolicy.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        AndroidInjection.inject(this) // injection happens here

userViewModel =        ViewModelProviders.of(this, viewModelFactory)[UserViewModel::class.java]
        validatorComposite = ValidatorComposite.getInstance(this)
        validatorComposite.add(field_address).add(field_firstname).add(field_lastname).add(field_phone_number).add(field_telphone_number)


        register.setOnClickListener({

            val message = checkFieldsValidation()
            if (message == null) {

                val registerUserModel = RegisterUserModel()
                registerUserModel.address= field_address.text.toString()
                registerUserModel.first_name= field_firstname.text.toString()
                registerUserModel.last_name= field_lastname.text.toString()
                registerUserModel.coordinate_mobile = field_phone_number.text.toString()
                registerUserModel.coordinate_phone_number = field_telphone_number.text.toString()

                val disposable: Disposable = userViewModel!!.registerUserData(registerUserModel).subscribeWith(DisposableCompletableObservers())
                compositeDisposable!!.add(disposable)

            }

            Toast.makeText(this@RegisterActivity, message.toString(), Toast.LENGTH_LONG).show()

        })


    }


    override fun onDestroy() {
        super.onDestroy()
        validatorComposite.clear()
        compositeDisposable?.dispose()
    }

    private fun checkFieldsValidation(): String? {

        for (input: EditTextWithPolicy in validatorComposite.list) {
            if (!input.isValid) {
                Log.d("@@@", input.message.plus("invalid"))
                return input.message
            }
        }

        return null
    }


    inner class DisposableCompletableObservers : DisposableCompletableObserver() {
        override fun onError(e: Throwable?) {


        }

        override fun onComplete() {

            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()

        }

    }


}
