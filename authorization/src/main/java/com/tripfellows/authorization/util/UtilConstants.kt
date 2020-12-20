package com.tripfellows.authorization.util

object UtilConstants {
    fun getServerURL(): String {
        return "http://10.0.2.2:8080/api/"
    }

    fun getTripPath(): String {
        return "trip/?"
    }

    fun getIdPath(): String {
        return "id="
    }

    fun getAndPathPart(): String {
        return "&"
    }

    fun getTripCreatorUidPath(): String {
        return "creatorUid="
    }
}