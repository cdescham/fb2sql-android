package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude

data class Event(
    val deactivated:Boolean? = null,
    val deleted:Boolean? = null,
    var reported:Boolean = false,
    val name:String? = null,
    val description:String? = null,
    val endDate:Long? = null,
    var photo:String? = null,
    val placeID:String? = null,
    val timestamp:Long? = null,
    val startDate:Long? = null,
    val location:String? = null,
    val userId:String? = null,
    val participants:List<String>? = null,
    val invited:List<String>? = null,
    val latitude:Double? = null,
    val longitude:Double? = null,
    val timestampEnd:Long? = null,
    @Exclude
    var identifier:String? = null,
    @Exclude
    var highlighted:Boolean? = false
)


