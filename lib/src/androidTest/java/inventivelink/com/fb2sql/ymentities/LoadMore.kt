package fr.heymum.yoomum.bo

import java.io.Serializable

/**
 * @author Thomas Ecalle
 * @since 2018.08.07
 */
data class LoadMore(
    val lastDateTimestamp:Long,
    var alreadyTriggered:Boolean
): Serializable
