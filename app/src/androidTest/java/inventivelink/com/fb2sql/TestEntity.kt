/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql

data class TestEntity (
    var s: String? = null,
    var b: Boolean? = null,
    var l: Long? = null,
    var d: Double? = null,
    val list:List<String>? = null
)