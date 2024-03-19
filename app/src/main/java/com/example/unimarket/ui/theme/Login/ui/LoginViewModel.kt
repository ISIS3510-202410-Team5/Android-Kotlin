package com.example.unimarket.ui.theme.Login.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    /*Solo Modifica el valor email para que despues este modifique _email, solo es modificable dentro
    del LoginViewModel*/

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable : LiveData<Boolean> = _loginEnable


    /*Siempre va a tener el ultimo valor que se escriba en las cajas de texto de password e Email*/
    fun onLoginChanged(email: String,password:String){
        _email.value= email
        _password.value= password
        _loginEnable.value=isValidEmail(email) && isValidPassword(password)
    }

    /*Verifica validez y devuelve un true or false*/
    private fun isValidEmail(email: String):Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String):Boolean =password.length>5


    fun onLoginSelected() {
        TODO("Not yet implemented")
    }


}