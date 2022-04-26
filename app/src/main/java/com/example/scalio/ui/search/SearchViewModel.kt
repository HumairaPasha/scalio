package com.example.scalio.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scalio.common.data.UserRepository
import com.example.scalio.common.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var userInput: String = ""

    private val _users = MutableLiveData<PagingData<User>>(PagingData.empty())
    val users = _users

    fun fetchUsers(keyword: String) {
        if (keyword.isNotEmpty()) {
            viewModelScope.launch {
                repository.fetchUsers(keyword).cachedIn(viewModelScope).collect {
                    _users.value = it
                }
            }
        }
    }

}



