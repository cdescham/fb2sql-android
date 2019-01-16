package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.util.*

/**
 * @author Thomas Ecalle
 * @since 2018.08.09
 */
interface FeedDisplayable
{
}

/*

data class FeedItem(
    @PropertyName("type")
    private val typeName:String = "unknown",
    @get:PropertyName("timestamp") @set:PropertyName("timestamp")
    var cocoaTimestamp:Long? = null,
    val eventId:String? = null,
    val userId:String? = null,
    val addressId:String? = null,
    var message:String? = null,
    val comments:HashMap<String, Comment>? = null,
    var likes:ArrayList<String>? = null,
    @get:PropertyName("timestampEnd") @set:PropertyName("timestampEnd")
    var cocoaTimestampEnd:Long? = null,
    @Exclude
    var identifier:String? = null,
    @Exclude
    var displayableEvent:DisplayableEvent? = null,
    @Exclude
    var displayableAddress:DisplayableAddress? = null,
    @Exclude
    var mum:Mum? = null,
    @Exclude
    var highlighted:Boolean? = false
): Cloneable, FeedDisplayable */


data class FeedItem(
        @PropertyName("type")
        private val typeName:String = "unknown",
        @get:PropertyName("timestamp") @set:PropertyName("timestamp")
        var cocoaTimestamp:Long? = null,
        val eventId:String? = null,
        val userId:String? = null,
        val addressId:String? = null,
        var message:String? = null,
        val comments:HashMap<String, Comment>? = null,
        var likes:ArrayList<String>? = null,
        @get:PropertyName("timestampEnd") @set:PropertyName("timestampEnd")
        var cocoaTimestampEnd:Long? = null,
        @Exclude
        var identifier:String? = null,
        @Exclude
        var displayableEvent:DisplayableEvent? = null,
        @Exclude
        var displayableAddress:DisplayableAddress? = null,
        @Exclude
        var mum:Mum? = null,
        @Exclude
        var highlighted:Boolean? = false
): Cloneable, FeedDisplayable

enum class FeedType(val value:String)
{

    EVENT("event"),
    MESSAGE("message"),
    MUM("mum"),
    ADDRESS("address"),
    UNKNOWN("unknown");

    override fun toString():String =
            value

    companion object
    {

        fun fromString(type:String):FeedType
        {
            return when (type)
            {
                "message" -> MESSAGE
                "mum"     -> MUM
                "event"   -> EVENT
                "address" -> ADDRESS
                else      -> UNKNOWN
            }
        }

    }

}