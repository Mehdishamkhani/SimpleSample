package org.sana.simpleapp.widget

/**
 * Created by mehdi on 19/10/2019.
 */

object Validation {

    private fun checkBasicValidation(input: String?): Boolean {
        return input != null && input.isNotEmpty()
    }

    private fun checkStringLength(input: String, minLength: Int, maxLength: Int): Boolean {
        return checkBasicValidation(input) && input.length >= minLength && input.length <= maxLength
    }

    fun checkGeneralValidation(input: String): Boolean {
        return checkStringLength(input, 3, 20)
    }

    fun checkAddressValidation(input: String): Boolean {
        return checkStringLength(input, 7, 30)
    }

    fun checkPhoneNumberValidation(input: String): Boolean {
        return checkBasicValidation(input) && "^(0|9)[0-9]{9,10}$".toRegex().matches(input)
    }

    fun checkTelNumberValidation(input: String): Boolean {
        return checkBasicValidation(input) && "^(0)[0-9]{9,10}$".toRegex().matches(input)
    }

}
