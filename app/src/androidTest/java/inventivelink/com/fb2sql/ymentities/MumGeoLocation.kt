package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude

/**
 * @author Anthony Msihid
 * @since 2018.09.13
 */
data class MumGeoLocation(
    val latitude:Double? = 48.866667,
    val longitude:Double? = 2.333333,

    @Exclude
    var mumIdentifier:String? = null
)