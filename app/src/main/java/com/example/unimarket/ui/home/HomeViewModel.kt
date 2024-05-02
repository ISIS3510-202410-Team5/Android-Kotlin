package com.example.unimarket.ui.home

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.unimarket.connection.ConnectivityObserver
import com.example.unimarket.connection.NetworkConnectivityObserver
import com.example.unimarket.model.User
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.Result
import com.example.unimarket.repositories.UserRepository
import com.example.unimarket.ui.ListProducts.ProductListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor
(
    private val userRepository: UserRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val application: Application
): ViewModel()
{
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow(String())
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _state: MutableState<UserListState> = mutableStateOf(UserListState())
    val state: State<UserListState> = _state

    val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)



    var isOnline: Flow<Boolean> = connectivityRepository.isConnected

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

        viewModelScope.launch {
            connectivityRepository.isConnected.collect() {
                Log.d("HomeViewModel", "$it")

            }
        }

        Log.d("HomeViewModel", "inicializa el viewModel")
        //var connected: Boolean = false
        //viewModelScope.launch { isOnline.collect() {
        //    connected = it
        //} }
        //if (connected) {
        getRegUsers()
        //}
    }

    fun getRegUsers()
    {
        Log.d("HomeViewModel", "getRegUsersCalled")
        val users = userRepository.getUserList().onEach { result ->

            when(result){
                is Result.Error -> {
                    _state.value = UserListState(error = result.message?: "Unknown error")
                }
                is Result.Loading -> {
                    _state.value = UserListState(isLoading = true)
                }
                is Result.Success -> {
                    _state.value = UserListState(users = result.data ?: User())
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}