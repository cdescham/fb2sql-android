package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude

/**
 * @author Anthony Msihid
 * @since 2018.09.13
 */
data class MumGeoLocation(
    val g:String? = null,
    val l:ArrayList<Double>? = ArrayList(),
    @Exclude
    var mumIdentifier:String? = null
)