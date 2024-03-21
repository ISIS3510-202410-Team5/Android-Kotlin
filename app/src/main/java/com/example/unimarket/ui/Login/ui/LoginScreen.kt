package com.example.unimarket.ui.Login.ui

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavHostController){
    Box(
        Modifier
            .fillMaxHeight()
            .padding(16.dp)) {
        Login(Modifier.align(Alignment.Center),viewModel, navController)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavHostController) {

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
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        WelcomeText()
        Spacer(modifier = Modifier.padding(8.dp))
        /*Cada vez que se oprima una tecla se llama al view model para comprobar si es valido*/
        EmailText()
        EmailSpace(email){viewModel.onLoginChanged(it,password)}
        Spacer(modifier = Modifier.padding(16.dp))
        PasswordText()
        PasswordSpace(password) {viewModel.onLoginChanged(email,it)}
        Spacer(modifier = Modifier.padding(16.dp))
        ButtonLogin(loginEnable){
            coroutineScope.launch {
                viewModel.onLoginSelected()}
            }

        Spacer(modifier = Modifier.padding(16.dp))
        SignUpText()
    }
    }
}

/*single source of thruth*/

@Composable
fun ButtonLogin(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    { TODO("Aqui toca colocar la parte de navegacion") }
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
        Text(text = "Sign In",
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif)
    }
}
@Composable
fun WelcomeText() {
    Text(text = "Sign In",
        fontSize = 25.sp,
        color = Color(0xFF181316),
        fontFamily = FontFamily.SansSerif
    )
    Text(text = "Hi there! Nice to see you again.",
        fontSize = 15.sp,
        color = Color(0xFF989EB1),
        fontFamily = FontFamily.SansSerif
    )
}


@Composable
fun SignUpText() {
    { TODO("Aqui toca colocar la parte de navegacion hacia Sign UP screen") }
    Text(text = "Sign Up",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable { },
        fontSize = 15.sp,
        color = Color(0xFFFF5958),
        fontFamily = FontFamily.SansSerif
        )
}

@Composable
fun EmailText() {
    Text(text = "Email",
        fontSize = 17.sp,
        color = Color(0xFFFF5958),
        fontFamily = FontFamily.SansSerif

    )
}

@Composable
fun PasswordText() {
    Text(text = "Password",
        fontSize = 20.sp,
        color = Color(0xFFFF5958),
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun PasswordSpace(password:String,onTextFieldChanged:(String)->Unit) {

    TextField(value = password, onValueChange = {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Password",
            color= Color(0xFF989EB1),
            fontFamily = FontFamily.SansSerif)},
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
        placeholder={ Text(text="Email",
            color = Color(0xFF989EB1),
            fontFamily = FontFamily.SansSerif) },
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
            .size(150.dp)
    )
}
