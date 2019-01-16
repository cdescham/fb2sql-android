package fr.heymum.yoomum.bo

import java.io.Serializable

/**
 *
 * @author Adrien Vitti
 * @since 2018.09.11
 */
data class MessageAuthor(
    private var identifier:String = "",
    private var shortName:String = "",
    private var avatarUri:String = ""
): Serializable