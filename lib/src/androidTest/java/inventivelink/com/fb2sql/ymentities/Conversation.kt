package fr.heymum.yoomum.bo

import com.google.firebase.database.Exclude
import java.io.Serializable

/**
s *
 * @author Adrien Vitti
 * @since 2018.08.28
 */
data class Conversation(
    @Exclude @set:Exclude @get:Exclude
    var key:String? = null,
    var userId:String? = null,
    var participants:Map<String, String>? = null,
    var leftParticipants:Map<String, String>? = null,
    var unreadStatus:Map<String, Int>? = null,
    var lastMessageTimestamp:Long? = null,
    var lastMessage:String? = null,
    var participantIds:String? = null,
    @set:Exclude @get:Exclude var participantsAvatar:Map<String, String>? = null
): Serializable
{

  companion object
  {

    fun computeParticipantIds(participants:Collection<String>):String
    {
      return participants.sorted().joinToString(separator = ":")
    }

  }

  @get:Exclude
  val isGroupConversation:Boolean
    get() = (this.participants?.size ?: 0) + (this.leftParticipants?.size ?: 0) > 2
  @get:Exclude
  val originalParticipants:Map<String, String>
    get() = this.participants.orEmpty().plus(leftParticipants.orEmpty())
  @get:Exclude
  val isBlankConversation:Boolean
    get() = this.lastMessage.isNullOrBlank()
        && (this.lastMessageTimestamp == null || this.lastMessageTimestamp == 0L)
        && this.unreadStatus == null

}