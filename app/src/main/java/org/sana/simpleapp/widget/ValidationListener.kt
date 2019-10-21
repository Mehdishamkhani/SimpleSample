package org.sana.simpleapp.widget

import org.sana.simpleapp.widget.EditTextWithPolicy

/**
 * Created by mehdi on 19/10/2019.
 */

interface ValidationListener {

    fun onValidationStatusChange(editTextWithPolicy: EditTextWithPolicy, isValid: Boolean)
}
