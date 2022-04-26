package com.example.scalio.api

import com.example.scalio.common.data.model.UserListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/search/users")
    suspend fun getUsers(
        @Query("q") login: String, @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): UserListResponse
}