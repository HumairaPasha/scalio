package com.example.scalio.common.data.model

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("total_count") var totalCount: Int = 0,
    @SerializedName("incomplete_results") var incompleteResults: Boolean = false,
    @SerializedName("items") var items: List<User> = arrayListOf()
)