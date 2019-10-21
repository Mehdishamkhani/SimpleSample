package org.sana.simpleapp.widget

/**
 * Created by mehdi on 19/10/2019.
 */

/**
 *  simple input policy validator
 */
class PolicyValidator {
    var isValid = false
        private set
    var message = "فیلد ها نمیتواند خالی باشد"
        private set

    fun validate(policy: String?, input: String): PolicyValidator {


        when (policy) {

            PHONE_POLICY -> {
                this.isValid = Validation.checkPhoneNumberValidation(input)
                this.message = "شماره همراه اشتباه وارد شده است"
            }

            TELEPHONE_POLICY -> {
                this.isValid = Validation.checkTelNumberValidation(input)
                this.message = "شماره تلفن ثابت اشتباه وارد شده است"
            }

            ADDRESS_POLICY -> {
                this.isValid = Validation.checkAddressValidation(input)
                this.message = "آدرس اشتباه وارد شده است"
            }

            else -> {
                this.isValid = Validation.checkGeneralValidation(input)
                this.message = "مقادیر ورودی باید بیش از سه حرف باشد"
            }
        }

        return this
    }

    companion object {


        const val GENERAL_POLICY = "general"
        private const val PHONE_POLICY = "phone"
        private const val TELEPHONE_POLICY = "telephone"
        private const val ADDRESS_POLICY = "address"


        fun init(): PolicyValidator {
            return PolicyValidator()
        }
    }


}
