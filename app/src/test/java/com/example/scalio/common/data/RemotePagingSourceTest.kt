package com.example.scalio.common.data

import androidx.paging.PagingSource
import com.example.scalio.api.UserService
import com.example.scalio.common.data.RemotePagingSource.Companion.IN_QUALIFIER
import com.example.scalio.common.data.model.User
import com.example.scalio.common.data.model.UserListResponse
import com.example.scalio.common.data.model.UserType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RemotePagingSourceTest {

    @Mock
    lateinit var userService: UserService

    @Test
    fun pagedRemoteSource_InitialLoad() = runTest {
        val dummyResponse = createDummyUserResponse()
        val users = dummyResponse.items
        Mockito.`when`(userService.getUsers(SAMPLE_QUERY + IN_QUALIFIER, 1, 3))
            .thenReturn(dummyResponse)
        val pagingSource = RemotePagingSource(SAMPLE_QUERY, userService)
        assertEquals(
            expected = PagingSource.LoadResult.Page(
                data = users.sortedBy { it.login },
                prevKey = null,
                nextKey = 1
            ), actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 3, false))
        )
    }

    @Test
    fun pagedRemoteSource_ReturnEmptyPage() = runTest {
        val emptyResponse = UserListResponse(0, false, listOf())
        Mockito.`when`(userService.getUsers(SAMPLE_QUERY + IN_QUALIFIER, 1, 3))
            .thenReturn(emptyResponse)
        val pagingSource = RemotePagingSource(SAMPLE_QUERY, userService)
        val prevKey: Int? = null

        assertEquals(
            expected = PagingSource.LoadResult.Page(
                data = emptyResponse.items,
                prevKey = prevKey,
                nextKey = prevKey
            ), actual = pagingSource.load(PagingSource.LoadParams.Refresh(1, 3, false))
        )

    }

    @Test
    fun testPageSourceException() = runTest {
        val pagingSource = RemotePagingSource(SAMPLE_QUERY, userService)
        val error = RuntimeException("404", Throwable())
        given(userService.getUsers(anyString(), anyInt(), anyInt())).willThrow(error)
        val expectedResult = PagingSource.LoadResult.Error<Int, User>(error)
        assertEquals(
            expected = expectedResult,
            actual = pagingSource.load(PagingSource.LoadParams.Refresh(null, 3, false))
        )
    }

    private fun createDummyUserResponse(): UserListResponse {
        val users = ArrayList<User>()
        users.add(User("http://", "Kate", UserType.ORGANIZATION))
        users.add(User("http://", "Jack", UserType.USER))
        users.add(User("http://", "Clementine", UserType.ORGANIZATION))

        return UserListResponse(2, false, users)
    }

    companion object {
        const val SAMPLE_QUERY = "test"
    }
}