package com.example.scalio.common.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scalio.api.UserService
import com.example.scalio.common.data.model.User

class RemotePagingSource(
    private val query: String,
    private val userService: UserService
) : PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER
        return try {
            val response = userService.getUsers(apiQuery, position, params.loadSize)
            val users = response.items.sortedBy { it.login }
            val nextKey = if (users.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)

            }
            LoadResult.Page(
                data = users,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val IN_QUALIFIER = "in:login"
        const val NETWORK_PAGE_SIZE = 9
    }
}