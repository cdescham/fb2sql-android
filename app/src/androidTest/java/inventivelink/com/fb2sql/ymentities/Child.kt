package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class Child(
    val name:String? = null,
    val sex:String? = null,
    @get:PropertyName("dateOfBirth") @set:PropertyName("dateOfBirth")
    var cocoaDateOfBirth:Long? = null,
    @get:Exclude
    var childKey:String? = null
)

