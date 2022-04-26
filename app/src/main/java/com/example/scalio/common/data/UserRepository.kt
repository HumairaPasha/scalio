package com.example.scalio.common.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scalio.api.UserService
import com.example.scalio.common.data.RemotePagingSource.Companion.NETWORK_PAGE_SIZE
import com.example.scalio.common.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {

    fun fetchUsers(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { RemotePagingSource(query, userService) }
        ).flow
    }
}