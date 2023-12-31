package org.sana.simpleapp.model

/**
 * Created by mehdi on 20/10/2019.
 */

data class UserDetailsDataModel(var region: Int = 1,
                                var address: String? = null,
                                var lat: Float? = 0.toFloat(),
                                var lng: Float? = 0.toFloat(),
                                var coordinate_mobile: String? = null,
                                var coordinate_phone_number: String? = null,
                                var first_name: String? = null,
                                var last_name: String? = null,
                                var gender: String? = null)


