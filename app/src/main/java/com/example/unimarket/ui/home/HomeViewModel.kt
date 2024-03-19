package com.example.unimarket.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow(String())
    val searchText: StateFlow<String> = _searchText.asStateFlow()

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
    }
}