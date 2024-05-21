package com.example.unimarket.ui.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.unimarket.repositories.PostRepository
import androidx.navigation.navArgument
import com.example.unimarket.repositories.UsuarioRepository
import com.example.unimarket.ui.Chats.ChatViewModel
import com.example.unimarket.ui.Chats.ListaDeChats
import com.example.unimarket.ui.Chats.VistaDelChat
import com.example.unimarket.ui.DetailProduct.DetailProduct
import com.example.unimarket.ui.ListProducts.ListProductApp
import com.example.unimarket.ui.SearchProduct.SearchProductApp
import com.example.unimarket.ui.Login.model.LoginModel
import com.example.unimarket.ui.home.Home
import com.example.unimarket.ui.Login.ui.LoginScreen
import com.example.unimarket.ui.Login.ui.LoginViewModel
import com.example.unimarket.ui.Login.ui.PasswordRecoverViewModel
import com.example.unimarket.ui.Login.ui.PasswordResetScreen
import com.example.unimarket.ui.Login.ui.SignUpScreen
import com.example.unimarket.ui.Login.ui.SignUpViewModel
import com.example.unimarket.ui.Login.ui.UserInfoScreen
import com.example.unimarket.ui.Login.ui.UserInfoViewModel
import com.example.unimarket.ui.SearchProduct.LocationSlider
import com.example.unimarket.ui.camera.ui.CameraScreen
import com.example.unimarket.ui.camera.ui.CameraViewModel
import com.example.unimarket.ui.camera.ui.LightSensorViewModel
import com.example.unimarket.ui.publishitem.PublishItem
import com.example.unimarket.ui.shoppingcart.ShoppingCart
import com.example.unimarket.ui.usuario.PerfilViewModel
import com.example.unimarket.ui.usuario.UserProfileScreen
import com.example.unimarket.ui.usuario.UsuarioScreen
import com.example.unimarket.ui.usuario.UsuarioViewModel
import com.example.unimarket.ui.usuario.shakeSlider

@Composable
fun Nav(lightSensorViewModel: LightSensorViewModel){

    val navController = rememberNavController()


    //Model declaration:
    val loginModel = LoginModel()
    /*val usuariorepository= UsuarioRepository()*/

    //This should be changed to a pattern
    val loginViewModel = remember {LoginViewModel(loginModel)}
    val signUpViewModel = remember {SignUpViewModel(loginModel)}
    val userInfoViewModel= remember {UserInfoViewModel(loginModel,signUpViewModel)}
    val cameraViewModel = remember {CameraViewModel()}
    /*val UsuarioViewModel = remember {UsuarioViewModel(usuariorepository)}*/

    val passwordrecoverviewmodel = remember {PasswordRecoverViewModel()}

    /*val UsuarioViewModel = remember {UsuarioViewModel(usuariorepository)}*/

    val chatViewModel: ChatViewModel = hiltViewModel()

    Scaffold (
        bottomBar = {AppBottomNav(navController = navController)}
    ) {
            innerPadding ->
        NavHost(navController, startDestination = Screen.LogIn.route, Modifier.padding(innerPadding)){
            composable(Screen.Home.route){
                Home(navController=navController)
            }
            composable(Screen.Post.route + "?imageUri={imageUri}",
                arguments = listOf(navArgument("imageUri") {defaultValue = ""})){
                val productUri = it.arguments?.getString("imageUri") ?: ""
                PublishItem(navController = navController, productUri, cameraViewModel)
            }
            composable(Screen.Camera.route){
                CameraScreen(viewModel = cameraViewModel, lightViewModel = lightSensorViewModel,navController=navController)
            }
            composable(Screen.Cart.route){
                ShoppingCart(navController = navController)
            }
            composable(Screen.UnderConstruction.route){
                Text(text="Under construction")
            }
            composable(Screen.LogIn.route){
                LoginScreen(viewModel = loginViewModel, navController = navController)
            }
            composable(Screen.SignUp.route){
                SignUpScreen(viewModel = signUpViewModel, navController = navController)
            }
            composable(Screen.ListProduct.route){
                ListProductApp(navController = navController)
            }
            composable(Screen.User.route){
                shakeSlider()
            }
            composable(Screen.ListProductSearch.route){
                SearchProductApp(navController = navController)
            }
            composable(Screen.DetailProduct.route + "/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                DetailProduct(navController = navController, productId = productId,chatViewModel = chatViewModel)
            }
            composable(Screen.InfoScreen.route) {
                UserInfoScreen(navController = navController, viewModel =userInfoViewModel)
            }
            composable(Screen.LocationSliderScreen.route) {
                LocationSlider(navController = navController)
            }

            composable(Screen.RecoverScreen.route) {
                PasswordResetScreen(navController = navController,passwordrecoverviewmodel )
            }
            composable(Screen.ListChats.route) {
                ListaDeChats(navController = navController, chatViewModel = chatViewModel)
            }
            composable(Screen.ChatDetail.route + "/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                VistaDelChat(chatId = chatId, chatViewModel = chatViewModel)
            }
            composable(Screen.PerfilScreen.route) {
                UsuarioScreen(navController = navController)
            }

            /*composable(Screen.UserProfile.route) {
                UserProfileScreen(navController = navController, usuarioViewModel = UsuarioViewModel,,)
            }*/
        }
    }

}

@Composable
fun AppBottomNav(navController: NavHostController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (currentDestination?.route == Screen.LogIn.route || currentDestination?.route == Screen.SignUp.route|| currentDestination?.route == Screen.InfoScreen.route|| currentDestination?.route == Screen.RecoverScreen.route)
    {
        Log.d(null, "La ruta actual es ${currentDestination.route}")
    } else {

        NavigationBar {
            BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->
                NavigationBarItem(selected = navigationItem.route == currentDestination?.route,
                    onClick = {
                        navController.navigate(navigationItem.route) {
                            launchSingleTop = true
                            restoreState = true
                            if (navigationItem.route == Screen.Home.route)
                            {
                                //popUpTo(Screen.LogIn.route){inclusive = true}
                                popUpTo(Screen.Home.route){inclusive = true}
                                Log.d(null,"popupto aplicado")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = navigationItem.icon,
                            contentDescription = navigationItem.label
                        )
                    })
            }
        }
    }

}


data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Rounded.Menu,
    val route : String = ""
) {
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Rounded.Home,
                route = Screen.Home.route
            ),
            BottomNavigationItem(
                label = "Msgs",
                icon = Icons.Rounded.MailOutline,
                route = Screen.ListChats.route
            ),
            BottomNavigationItem(
                label = "Post",
                icon = Icons.Rounded.AddCircle,
                route = Screen.Post.route
            ),
            BottomNavigationItem(
                label = "Cart",
                icon = Icons.Rounded.ShoppingCart,
                route = Screen.Cart.route

            ),
            BottomNavigationItem(
                label = "User",
                icon = Icons.Rounded.AccountCircle,
                route = Screen.PerfilScreen.route

            )

        )
    }
}




sealed class Screen(val route: String) {
    data object Home: Screen("HOME")
    data object Post: Screen(route = "POST")
    data object Cart: Screen(route = "CART")
    data object UnderConstruction: Screen(route = "UNDER")
    data object UserProfile: Screen(route = "USER")
    data object LogIn: Screen(route= "LOGIN")
    data object SignUp: Screen(route = "SIGNUP")
    data object ListProduct: Screen(route = "LIST")

    data object Camera: Screen(route = "CAMERA")

    data object User: Screen(route = "USER")
    data object ListProductSearch: Screen(route = "SEARCH")

    data object DetailProduct: Screen(route = "DETAIL")

    data object InfoScreen: Screen(route = "INFO")

    data object RecoverScreen: Screen(route = "RECOVER")

    data object PerfilScreen: Screen(route = "PERFIL")

    data object LocationSliderScreen: Screen(route = "SliderLocation")

    data object ListChats: Screen(route = "LISTCHATS")

    data object ChatDetail: Screen(route = "CHAT")
}