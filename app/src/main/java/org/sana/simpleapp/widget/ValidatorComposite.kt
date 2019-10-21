package org.sana.simpleapp.utils

import org.sana.simpleapp.widget.EditTextWithPolicy
import java.util.*

/**
 * Created by mehdi on 19/10/2019.
 */

class ValidatorComposite<T : EditTextWithPolicy> private constructor(private val validationListener: ValidationListener?) {
    private val viewList = ArrayList<T>()
    val list: List<T>
        get() = viewList


    fun add(view: T): ValidatorComposite<T> {

        if (validationListener != null)
            view.setValidationListener(validationListener)

        viewList.add(view)

        return this
    }

    fun remove(view: T) {
        viewList.remove(view)
    }

    fun clear() {
        validatorComposite = null
        viewList.clear()
    }

    companion object {

        private var validatorComposite: ValidatorComposite<in EditTextWithPolicy>? = null;

        fun getInstance(validationListener: ValidationListener): ValidatorComposite<in EditTextWithPolicy>? {

            if (validatorComposite == null)
                synchronized(ValidatorComposite::class) {
                    if (validatorComposite == null) {
                        validatorComposite = ValidatorComposite(validationListener)
                    }
                }

            return validatorComposite
        }
    }


}
