package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude

data class Child(
        var childId:String? = null,
        var name:String? = null,
        var sex:String? = null,
        var dateOfBirth:Long? = null,
        @get:Exclude
        var childKey:String? = null
)

