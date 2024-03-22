package com.example.unimarket.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.repositories.Result
import com.example.unimarket.repositories.UserRepository
import com.example.unimarket.ui.ListProducts.ProductListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor
(
    private val userRepository: UserRepository
): ViewModel()
{
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow(String())
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _state: MutableState<UserListState> = mutableStateOf(UserListState())
    val state: State<UserListState> = _state

    fun onSearchTextChange(text:String){
        _searchText.value = text
        Log.d(null, _searchText.value)
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    init {
        Log.d(null, "inicializa el viewModel")
        getRegUsers()
    }

    fun getRegUsers()
    {
        val users = userRepository.getUserList().onEach { result ->

            when(result){
                is Result.Error -> {
                    _state.value = UserListState(error = result.message?: "Unknown error")
                }
                is Result.Loading -> {
                    _state.value = UserListState(isLoading = true)
                }
                is Result.Success -> {
                    _state.value = UserListState(users = result.data ?: emptyList())
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}