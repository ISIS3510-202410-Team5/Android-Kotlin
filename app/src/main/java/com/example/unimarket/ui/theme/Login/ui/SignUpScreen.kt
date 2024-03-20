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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.text.input.KeyboardType
import com.example.unimarket.R
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(viewModel: SignUpViewModel){
    Box(
        Modifier
            .fillMaxHeight()
            .padding(16.dp)) {
        SignUp(Modifier.align(Alignment.Center),viewModel)
    }
}

@Composable
fun SignUp(modifier: Modifier, viewModel: SignUpViewModel) {

    val email:String by viewModel.email.observeAsState(initial = "")
    val password:String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)

    val coroutineScope= rememberCoroutineScope()
    val isloading:Boolean by viewModel.isloading.observeAsState(initial=false)

    if (isloading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(modifier.align(Alignment.Center))
        }}
    else{
        Column (modifier= modifier) {
            HeaderImageSignUp(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.padding(16.dp))
            /*Cada vez que se oprima una tecla se llama al view model para comprobar si es valido*/
            EmailSignUp(email){viewModel.onLoginChanged(it,password)}
            Spacer(modifier = Modifier.padding(16.dp))
            PasswordCreationSpace(password) {viewModel.onLoginChanged(email,it)}
            Spacer(modifier = Modifier.padding(16.dp))
            ButtonSignUp(loginEnable){
                coroutineScope.launch {
                    viewModel.onLoginSelected()}
            }

            Spacer(modifier = Modifier.padding(8.dp))
            BienvenidoUni     ()
        }
    }
}

/*single source of thruth*/

@Composable
fun ButtonSignUp(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(onClick = { onLoginSelected() }, modifier = Modifier
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
        Text(text = "Sign up")
    }
}


@Composable
fun BienvenidoUni() {
    Text(text = "Bienvenido a Unimarket",
        modifier = Modifier.clickable { },
        fontSize = 12.sp,
        color = Color(0xFF989EB1)
    )
}

@Composable
fun PasswordCreationSpace(password:String,onTextFieldChanged:(String)->Unit) {
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
fun EmailSignUp(email:String,onTextFieldChanged:(String)->Unit) {
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
fun HeaderImageSignUp(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Header",
        modifier= modifier
            .size(200.dp)
    )
}
