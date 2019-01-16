package fr.heymum.yoomum.bo

import java.io.Serializable

/**
 * @author Thomas Ecalle
 * @since 2018.08.02
 */
data class SelectableMum(
    val mum:Mum? = null,
    var isSelected:Boolean = false
): Serializable
{

  override fun equals(other:Any?):Boolean
  {
    if (this === other)
    {
      return true
    }

    return if (other is SelectableMum)
    {
      mum?.identifier == other.mum?.identifier
    }
    else
    {
      false
    }
  }
}
