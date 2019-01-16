package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Comment(
    @Exclude
    var key:String? = null,
    val childrenInfo:String? = null,
    val comment:String? = null,
    val dateOfBirth:Long? = null,
    val name:String? = null,
    val timestamp:Long? = null,
    val userId:String? = null,
    @Exclude var mum:Mum? = null
): Serializable


