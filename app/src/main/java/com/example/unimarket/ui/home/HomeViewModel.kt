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
import com.example.unimarket.entities.CategoryEntity
import com.example.unimarket.model.User
import com.example.unimarket.repositories.CategoryRepository
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.Result
import com.example.unimarket.repositories.UserRepository
import com.example.unimarket.ui.ListProducts.ProductListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor
(
    private val userRepository: UserRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val categoryRepository: CategoryRepository,
    private val application: Application
): ViewModel()
{

    /*
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow(String())
    val searchText: StateFlow<String> = _searchText.asStateFlow()

     */

    private val _state: MutableState<UserListState> = mutableStateOf(UserListState())
    val state: State<UserListState> = _state

    private val _categories = MutableStateFlow<List<CategoryEntity>>(value = listOf())
    val categories = _categories

    val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)



    var isOnline: Flow<Boolean> = connectivityRepository.isConnected

    /*fun onSearchTextChange(text:String){
        _searchText.value = text
        Log.d(null, _searchText.value)
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

     */

    init {

        viewModelScope.launch {
            connectivityRepository.isConnected.collect() {
                Log.d("HomeViewModel", "$it")
                if (it){
                    Log.d("HomeViewModel", "entra al if con $it")
                    getCategories()
                }
                else {
                    getCategoriesDB()
                }

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
                    Log.d("HomeViewModel", "getRegUsers() success")
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun getCategories()
    {
        Log.d("HomeViewModel", "getCategoriesCalled")

        val res = categoryRepository.getCategoriesFirebase().onEach {
            result ->

            when(result)
            {
                is Result.Error -> {
                    Log.d("HomeViewModel", "Error result in getting Categories")
                }
                is Result.Loading -> {
                    Log.d("HomeViewModel", "Loading")
                }
                is Result.Success -> {
                    Log.d("HomeViewModel", "Worked Correctly")
                    getCategoriesDB()
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }


    fun getCategoriesDB() {
        Log.d("HomeViewModel", "getCategoriesDB() called")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _categories.value = categoryRepository.getCategoriesDB()
                Log.d("HomeViewModel", _categories.value.toString())
            }
        }
    }
}