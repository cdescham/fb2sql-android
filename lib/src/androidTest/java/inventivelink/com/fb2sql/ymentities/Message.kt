package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Message(
    @Exclude @set:Exclude @get:Exclude
    var key:String? = null,
    var firstName:String? = null,
    var message:String? = null,
    var read:Map<String, Boolean>? = null,
    var timestamp:Long? = null,
    var userId:String? = null,
    @get:PropertyName("type") @set:PropertyName("type")
    var typeName:String? = null,
    @set:Exclude @get:Exclude var author:MessageAuthor? = null,
    @set:Exclude @get:Exclude var isFromGroupConversation:Boolean = false
): Serializable