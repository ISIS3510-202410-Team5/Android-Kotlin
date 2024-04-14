package com.example.unimarket.ui.navigation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Favorite
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.unimarket.R
import com.example.unimarket.ui.ListProducts.ListProductApp
import com.example.unimarket.ui.ListProducts.SearchProductApp
import com.example.unimarket.ui.Login.model.LoginModel
import com.example.unimarket.ui.home.Home
import com.example.unimarket.ui.home.HomeViewModel
import com.example.unimarket.ui.Login.ui.LoginScreen
import com.example.unimarket.ui.Login.ui.LoginViewModel
import com.example.unimarket.ui.Login.ui.SignUpScreen
import com.example.unimarket.ui.Login.ui.SignUpViewModel
import com.example.unimarket.ui.camera.ui.CameraScreen
import com.example.unimarket.ui.camera.ui.CameraViewModel
import com.example.unimarket.ui.camera.ui.LightSensorViewModel
import com.example.unimarket.ui.publishitem.PublishItem
import com.example.unimarket.ui.publishitem.PublishItemViewModel
import com.example.unimarket.ui.shoppingcart.ShoppingCart
import com.example.unimarket.ui.shoppingcart.ShoppingCartViewModel

@Composable
fun Nav(lightSensorViewModel: LightSensorViewModel){

    val navController = rememberNavController()


    //Model declaration:
    val loginModel = LoginModel()

    //This should be changed to a pattern
    val loginViewModel = remember {LoginViewModel(loginModel)}
    val signUpViewModel = remember {SignUpViewModel(loginModel)}

    Scaffold (
        bottomBar = {AppBottomNav(navController = navController)}
    ) {
        innerPadding ->
        NavHost(navController, startDestination = Screen.LogIn.route, Modifier.padding(innerPadding)){
            composable(Screen.Home.route){
                Home(navController=navController)
            }
            composable(Screen.Post.route){
                PublishItem(navController = navController)
            }
            composable(Screen.Camera.route){
                CameraScreen(viewModel = CameraViewModel(), lightViewModel = lightSensorViewModel)
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
            composable(Screen.ListProductSearch.route){
                SearchProductApp(navController = navController)
            }
        }
    }

}

@Composable
fun AppBottomNav(navController: NavHostController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (currentDestination?.route == Screen.LogIn.route || currentDestination?.route == Screen.SignUp.route)
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
                route = Screen.UnderConstruction.route
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
                route = Screen.UnderConstruction.route

            )

        )
    }
}




sealed class Screen(val route: String) {
    data object Home: Screen("HOME")
    data object Post: Screen(route = "POST")
    data object Cart: Screen(route = "CART")
    data object UnderConstruction: Screen(route = "UNDER")
    data object LogIn: Screen(route= "LOGIN")
    data object SignUp: Screen(route = "SIGNUP")
    data object ListProduct: Screen(route = "LIST")

    data object Camera: Screen(route = "CAMERA")

    data object ListProductSearch: Screen(route = "SEARCH")
}