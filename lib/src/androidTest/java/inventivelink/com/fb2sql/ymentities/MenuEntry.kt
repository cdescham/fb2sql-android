package fr.heymum.yoomum.bo

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

/**
 * @author Anthony Msihid
 * @since 2018.06.28
 */
abstract class MenuEntry(
    @StringRes open val stringRes:Int,
    @DrawableRes open val drawableRes:Int,
    @ColorRes open val colorRes:Int
)

data class MenuEntryFragment(
    @StringRes override val stringRes:Int,
    @DrawableRes override val drawableRes:Int,
    @ColorRes override val colorRes:Int,
    val classToOpen:Class<*>? = null
): MenuEntry(stringRes, drawableRes, colorRes)

data class MenuEntryAction(
    @StringRes override val stringRes:Int,
    @DrawableRes override val drawableRes:Int,
    @ColorRes override val colorRes:Int,
    val action:MenuAction
): MenuEntry(stringRes, drawableRes, colorRes)

enum class MenuAction
{

  SHARE,
  RATE,
  CONTACT,
  DISCONNECT
}