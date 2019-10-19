package org.sana.simpleapp.utils

import org.sana.simpleapp.widget.EditTextWithPolicy

import java.util.ArrayList

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
        viewList.clear()
    }

    companion object {
        fun  getInstance(validationListener: ValidationListener): ValidatorComposite<EditTextWithPolicy> {
            return ValidatorComposite<EditTextWithPolicy>(validationListener)
        }
    }


}
