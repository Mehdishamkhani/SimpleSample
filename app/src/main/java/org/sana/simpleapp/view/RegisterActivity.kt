package org.sana.simpleapp.view

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import kotlinx.android.synthetic.main.activity_register.*
import org.sana.simpleapp.model.UserDetailsDataModel
import org.sana.simpleapp.utils.AppUtils
import org.sana.simpleapp.utils.HttpHelper
import org.sana.simpleapp.viewmodel.UserViewModel
import org.sana.simpleapp.viewmodel.ViewModelFactory
import org.sana.simpleapp.widget.EditTextWithPolicy
import org.sana.simpleapp.widget.ValidationListener
import org.sana.simpleapp.widget.ValidatorComposite
import org.sanasimpleapp.R
import javax.inject.Inject

/**
 * Created by mehdi on 19/10/2019.
 */

/**
 *
 * Provides a Registration Fields & Gets User's Info And Send It To the Server
 *
 */
class RegisterActivity : BaseActivity(), ValidationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private var validatorComposite: ValidatorComposite<EditTextWithPolicy>? = null


    var lat: Float? = 0.toFloat()
    var lng: Float? = 0.toFloat()
    var address: String? = ""

    override fun onValidationStatusChange(editTextWithPolicy: EditTextWithPolicy, isValid: Boolean) {

        val drawable: Drawable? = if (isValid)
            ContextCompat.getDrawable(this, R.drawable.check_circle_green)
        else
            ContextCompat.getDrawable(this, R.drawable.unchecked_circle_gray)

        editTextWithPolicy.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        AndroidInjection.inject(this)

        val extras = intent?.extras;
        lat = extras?.getFloat(AppUtils.LAT_TAG)
        lng = extras?.getFloat(AppUtils.LNG_TAG)
        address = extras?.getString(AppUtils.ADDRESS_TAG)
        field_address.setText(address)

        userViewModel = ViewModelProviders.of(this, viewModelFactory)[UserViewModel::class.java]

        validatorComposite = ValidatorComposite.getInstance(this)
        validatorComposite?.add(field_firstname)?.add(field_lastname)?.add(field_phone_number)?.add(field_telephone_number)?.add(field_address)




        register.setOnClickListener {

            register.isEnabled = false
            val message = checkFieldsValidation()
            if (message == null) {

                val registerUserModel = UserDetailsDataModel()
                registerUserModel.address = field_address.text.toString()
                registerUserModel.first_name = field_firstname.text.toString()
                registerUserModel.last_name = field_lastname.text.toString()
                registerUserModel.coordinate_mobile = field_phone_number.text.toString()
                registerUserModel.coordinate_phone_number = field_telephone_number.text.toString()
                registerUserModel.lat = lat
                registerUserModel.lng = lng
                val gender = findViewById<RadioButton>(toggle_gender.checkedRadioButtonId).text
                registerUserModel.gender = if (gender == getString(R.string.male)) "Male" else "Female"

                val disposable: Disposable = userViewModel.registerUserData(registerUserModel).subscribeWith(DisposableCompletableObservers())
                compositeDisposable.add(disposable)

                return@setOnClickListener
            }

            Toast.makeText(this@RegisterActivity, message.toString(), Toast.LENGTH_LONG).show()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        validatorComposite?.clear()
        compositeDisposable.dispose()
    }

    private fun checkFieldsValidation(): String? {

        for (input: EditTextWithPolicy in validatorComposite!!.list) {
            if (!input.isValid) {
                Log.d("@@@", input.getMessage().plus("invalid"))
                return input.getMessage()
            }
        }

        return null
    }


    inner class DisposableCompletableObservers : DisposableCompletableObserver() {
        override fun onError(e: Throwable) {
            Toast.makeText(this@RegisterActivity, HttpHelper.showError(this@RegisterActivity, e), Toast.LENGTH_LONG).show()
            register.isEnabled = true

        }


        override fun onComplete() {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }

    }


}
