package fr.heymum.yoomum.bo

/**
 * @author Antoine Gerard
 * @since 2018.09.06
 */
data class DisplayableAddress(
    val address:Address,
    val amountOfComment:Long,
    var feedIdentifier:String? = null
): Displayable, Cloneable
{

  override fun isDisplayable():Boolean
  {
    return address.deactivated == null || address.deactivated.not()
  }

  public override fun clone():DisplayableAddress
  {
    return (super.clone() as DisplayableAddress)
  }
}
