package com.example.scalio.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.scalio.common.data.UserRepository
import com.example.scalio.common.data.model.User
import com.example.scalio.common.data.model.UserType
import com.example.scalio.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertNotEquals

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Mock
    lateinit var userRepository: UserRepository

    lateinit var viewModel: SearchViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        viewModel = SearchViewModel(userRepository)
    }

    @Test
    fun testFetchUsers() {

        viewModel.fetchUsers("test")
        Mockito.verify(userRepository).fetchUsers("test")
    }

    @Test
    fun fetchingValidQuery() {
        val list = listOf(User("http://", "hello", UserType.USER))
        val dummyPage = PagingData.from(list)

        val pagingFlow = flow { emit(dummyPage) }

        val observer = Observer<PagingData<User>> {}
        try {
            Mockito.`when`(userRepository.fetchUsers("test")).thenReturn(pagingFlow)
            viewModel.users.observeForever(observer)
            viewModel.fetchUsers("test")

            val value = viewModel.users.value
            assertNotEquals(value, PagingData.empty())

        } finally {
            viewModel.users.removeObserver(observer)

        }
    }

}