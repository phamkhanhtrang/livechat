package com.example.livechat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
 import com.example.livechat.CheckSignedIn
import com.example.livechat.CommonProgressBar
import com.example.livechat.DestinationScreen
import com.example.livechat.LCViewModel
import com.example.livechat.R
import com.example.livechat.navigateTo

@Composable
fun SignUpScreen(navController: NavController,vm: LCViewModel)
{
    CheckSignedIn(vm = vm, navController = navController )
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .verticalScroll(
                rememberScrollState()
            ),
            horizontalAlignment = Alignment.CenterHorizontally) {
            val nameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val numberState = remember {
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current
            Image(painter = painterResource(id = R.drawable.chat),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp))
            Text(text = "Sign Up", fontSize = 30.sp)
            OutlinedTextField(
                value =nameState.value ,
                onValueChange = {
                nameState.value = it
            },
                label = { Text(text = "Name")})
            OutlinedTextField(
                value =numberState.value ,
                onValueChange = {
                    numberState.value = it
                },
                label = { Text(text = "Name")})
            OutlinedTextField(
                value =emailState.value ,
                onValueChange = {
                    emailState.value = it
                },
                label = { Text(text = "Email")})

            OutlinedTextField(
                value =passwordState.value ,
                onValueChange = {
                    passwordState.value = it
                },
                label = { Text(text = "Pass")})
            
            Button(onClick = { vm.signUp(
                       name =  nameState.value.text,
                       number =  numberState.value.text,
                       email = emailState.value.text,
                      password= passwordState.value.text,
            )

            }) {
                Text(text = "Sign Up ")
            }
            Text(text = " Go to login",
                modifier = Modifier.clickable {
                    navigateTo(navController,DestinationScreen.Login.route)
                })
        }
    }
    if(vm.inProcess.value){
        CommonProgressBar()
    }
}
