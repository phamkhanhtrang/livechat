package com.example.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.livechat.Screens.ChatListScreen
import com.example.livechat.Screens.LoginScreen
import com.example.livechat.Screens.ProfileScreen
import com.example.livechat.Screens.SignUpScreen
import com.example.livechat.Screens.SingleChatScreen
import com.example.livechat.Screens.SingleStatusScreen
import com.example.livechat.Screens.StatusScreen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route : String){
    object SignUp: DestinationScreen("signup")
    object Login: DestinationScreen("login")
    object Profile: DestinationScreen("profile")
    object ChatList: DestinationScreen("chatlist")
    object SingleChat: DestinationScreen("singleChat/{chatID}"){
        fun createRoute(id: String ) = "singlechat/$id"
    }
    object StatusList: DestinationScreen("StatusList")
    object SingleStatus: DestinationScreen("singleStatus/{userID}"){
        fun createRoute(userId: String ) = "singleStatus/$userId"
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                 }
            }
        }
    }
    @Composable
    fun ChatAppNavigation(){
        val navControler = rememberNavController()
        var vm = hiltViewModel<LCViewModel>()
        NavHost(navController = navControler, startDestination = DestinationScreen.SignUp.route ){
            composable(DestinationScreen.SignUp.route){
                SignUpScreen(navControler,vm)
            }
            composable(DestinationScreen.Login.route){
               LoginScreen( navController =navControler , vm = vm)
            }
            composable(DestinationScreen.ChatList.route){
                ChatListScreen(navController = navControler,vm = vm )
            }
            composable(DestinationScreen.SingleChat.route){
                val chatID = it.arguments?.getString("chatID")
                chatID?.let {
                    SingleChatScreen(navController = navControler, vm = vm, chatId = chatID)
                }
            }
            composable(DestinationScreen.StatusList.route){
                StatusScreen(navController = navControler,vm = vm )
            }
            composable(DestinationScreen.Profile.route){
                ProfileScreen(navController = navControler,vm = vm )
            }
            composable(DestinationScreen.SingleStatus.route){
                val userId = it.arguments?.getString("userId")
                userId?.let {
                    SingleStatusScreen(navController = navControler, vm = vm, userId = it )            }

            }
        }

    }
}

