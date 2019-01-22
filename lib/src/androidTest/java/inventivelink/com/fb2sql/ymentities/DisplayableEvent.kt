package fr.heymum.yoomum.bo

/**
 * @author Antoine Gerard
 * @since 2018.08.31
 */
data class DisplayableEvent(
    val identifier:String,
    var event:Event,
    val isMumParticipating:Boolean,
    var commentAmount:Int = 0,
    val avatarUris:MutableList<String?>,
    val eventCreator:String?,
    val displayBadge:Boolean,
    var feedIdentifier:String? = null
): Displayable, Cloneable
{

  public override fun clone():DisplayableEvent
  {
    return (super.clone() as DisplayableEvent)
  }

  override fun isDisplayable():Boolean
  {
    return event.deleted == null || event.deleted == false
  }
}
