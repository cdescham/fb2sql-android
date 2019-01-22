package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude

/**
 * @author Anthony Msihid
 * @since 2018.09.13
 */
data class MumGeoLocation(
    var latitude:Double? = 48.866667,
    var longitude:Double? = 2.333333,
    var mumIdentifier:String? = null
)