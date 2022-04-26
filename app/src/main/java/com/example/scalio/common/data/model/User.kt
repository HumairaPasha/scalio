package com.example.scalio.common.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar_url")
    var avatarUrl: String? = "",
    var login: String? = null,
    var type: UserType? = UserType.UNDEFINED
)

enum class UserType(val label: String) {
    @SerializedName("Organization")
    ORGANIZATION("Organization"),

    @SerializedName("User")
    USER("User"),
    UNDEFINED("Undefined");
}