package fr.heymum.yoomum.bo

/**
 * @author Thomas Ecalle
 * @since 2018.09.21
 */
data class PlaceAutoComplete(val placeId:String, val addressKey:String?, val name:String?, val shortDescription:String?, val address:String?, val isAlreadyInYoomum:Boolean)