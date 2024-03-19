package com.example.unimarket.ui.theme.Login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.KeyboardType
import com.example.unimarket.R
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.sp


@Composable
fun LoginScreen(viewModel: LoginViewModel){
    Box(
        Modifier
            .fillMaxHeight()
            .padding(16.dp)) {
        Login(Modifier.align(Alignment.Center),viewModel)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {

    val email:String by viewModel.email.observeAsState(initial = "")
    val password:String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)


    Column (modifier= modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        /*Cada vez que se oprima una tecla se llama al view model para comprobar si es valido*/
        EmailSpace(email){viewModel.onLoginChanged(it,password)}
        Spacer(modifier = Modifier.padding(16.dp))
        PasswordSpace(password) {viewModel.onLoginChanged(email,it)}
        Spacer(modifier = Modifier.padding(16.dp))
        ButtonLogin(loginEnable){viewModel.onLoginSelected()}
        Spacer(modifier = Modifier.padding(8.dp))
        ForgotPassword()
    }

}

/*single source of thruth*/

@Composable
fun ButtonLogin(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(onClick = { onLoginSelected }, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF5958),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xFF989EB1),
            disabledContentColor = Color(0xFFFFFFFF)
        ), enabled = loginEnable
        )

    {
        Text(text = "Sign in")
    }
}

@Composable
fun ForgotPassword() {
    Text(text = "Recuperar contraseña",
        modifier = Modifier.clickable { },
        fontSize = 12.sp,
        color = Color(0xFF989EB1)
        )
}

@Composable
fun PasswordSpace(password:String,onTextFieldChanged:(String)->Unit) {
    TextField(value = password, onValueChange = {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Password",color= Color(0xFF989EB1))},
        keyboardOptions = KeyboardOptions (keyboardType= KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF181316),
            focusedContainerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
        )
}

@Composable
fun EmailSpace(email:String,onTextFieldChanged:(String)->Unit) {
    TextField(value = email ,
        onValueChange = {onTextFieldChanged(it)},
        modifier= Modifier.fillMaxWidth(),
        placeholder={ Text(text="Email", color = Color(0xFF989EB1)) },
        keyboardOptions = KeyboardOptions (keyboardType= KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF181316),
            focusedContainerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

    )


}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Header",
        modifier= modifier
    )
}
