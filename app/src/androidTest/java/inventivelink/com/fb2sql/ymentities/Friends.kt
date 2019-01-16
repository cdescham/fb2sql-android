package fr.heymum.yoomum.bo

import com.google.firebase.database.PropertyName

/**
 * @author Antoine Gerard
 * @since 2018.08.08
 */
data class Friends(
    val accepted:List<String> = ArrayList(),
    @get:PropertyName("accepted-notifications") @set:PropertyName("accepted-notifications") var acceptedNotifications:List<String> = ArrayList(),
    val sent:List<String> = ArrayList(),
    val received:List<String> = ArrayList()
)

enum class FriendStatusType
{

  none,
  received,
  sent,
  accepted,
  acceptedNotification
}