package fr.heymum.yoomum.bo

import com.google.firebase.database.PropertyName

data class Address(
        var addressKey:String? = null,
        val deactivated:Boolean? = null,
        val description:String? = null,
        val latitude:Double? = null,
        val longitude:Double? = null,
        val location:String? = null,
        val name:String? = null,
        val placeID:String? = null,
        var photo:String? = null,
        @get:PropertyName("timestamp") @set:PropertyName("timestamp")
        var cocoaTimestamp:Long? = null,
        val type:String? = null,
        val userId:String? = null
)

