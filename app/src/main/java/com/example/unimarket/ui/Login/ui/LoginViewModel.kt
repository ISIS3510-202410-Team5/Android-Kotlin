package com.example.unimarket.ui.Login.ui


import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.ui.Login.model.LoginModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val firebaseManager: LoginModel) : ViewModel() {
    /*Solo Modifica el valor email para que despues este modifique _email, solo es modificable dentro
    del LoginViewModel*/
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable : LiveData<Boolean> = _loginEnable

    private val _isloading = MutableLiveData<Boolean>()
    val isloading : LiveData<Boolean> = _isloading


    /*Siempre va a tener el ultimo valor que se escriba en las cajas de texto de password e Email*/
    fun onLoginChanged(email: String,password:String){
        _email.value= email
        _password.value= password
        _loginEnable.value=isValidEmail(email) && isValidPassword(password)
    }

    /*Verifica validez y devuelve un true or false*/
    private fun isValidEmail(email: String):Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String):Boolean =password.length>5

    suspend fun signinfire(email: String, password: String) {
        val emailValue = _email.value
        val passwordValue = _password.value
        if (emailValue != null && passwordValue != null) {
            firebaseManager.signIn(emailValue, passwordValue)
        }
    }

    suspend fun onLoginSelected() {
        _isloading.value=true
        val emailValue = _email.value
        val passwordValue = _password.value
        if (emailValue != null && passwordValue != null) {
            try {
                firebaseManager.signIn(emailValue, passwordValue)
            } catch (e: Exception) {
                /*en caso de que las credenciales no esten bien se crashe*/
                Log.e(TAG, "Error signing in: ${e.message}", e)
            }
            delay(1000)
            _isloading.value=false
        }
        { TODO("Aqui toca colocar la parte de navegacion") }


    }}

