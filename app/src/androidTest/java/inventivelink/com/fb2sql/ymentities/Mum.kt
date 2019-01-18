package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude

class Mum(
    var mumId : String ? = null,
    val firstName:String? = null,
    val lastName:String? = null,
    var photo:String? = null,
    var dateOfBirth:Long? = null,
    val email:String? = null,
    val phoneNumber:String? = null,
    val postalCode:String? = null,
    val reported:Boolean? = null,
    val street:String? = null,
    val yooMumAccount:Boolean? = null,
    val message:String? = null,
    val status:String? = null,
    val state:String? = null,
    val deactivated:Boolean? = null,
    var children:List<Child>? =null,
    val interests:List<String>? = mutableListOf(),
    val locations:List<String>? = mutableListOf(),
    var location:MumGeoLocation? = null,
    val events:MutableList<String>? = mutableListOf(),
    val eventsInvited:MutableList<String>? = mutableListOf(),
    val conversations:List<String>? = mutableListOf(),
    val friends:Friends? = Friends(),
    @set:Exclude @get:Exclude var friendStatus:FriendStatusType? = null,
    val favoriteLocations:MutableList<String>? = mutableListOf(),
    @set:Exclude @get:Exclude var identifier:String? = null,
    @set:Exclude @get:Exclude var feedIdentifier:String? = null,
    @set:Exclude @get:Exclude var feedType:FeedType? = null,
    var lastConnectionTimestamp:Long? = null
)
