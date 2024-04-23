package com.coolgirl.poctokkotlin.Screen.Login

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.R

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    SetLoginScreen(navController, viewModel)
}

@Composable
fun SetLoginScreen(navController: NavController, viewModel: LoginViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.blue)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.73f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.88f),
                shape = RoundedCornerShape(55.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.white)),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        SetRegistrationPage(navController, viewModel)
                    }
                }
            }
        }
        SetLoginButton(viewModel)
    }
}

@SuppressLint("ResourceType")
@Composable
fun SetRegistrationPage(navController: NavController, viewModel: LoginViewModel){
    key(viewModel.change){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center){
            Text(text = stringResource(id = viewModel.GetHeadText()), fontSize = 28.sp, color = colorResource(R.color.brown))
        }
        Text(text = "Логин:", color = colorResource(R.color.brown), fontSize = 18.sp)

        BasicTextField(
            value = viewModel.userLogin,
            onValueChange = { viewModel.UpdateUserLogin(it) },
            textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
            modifier = Modifier.height(40.dp).fillMaxWidth().background((colorResource(R.color.blue)), shape = RoundedCornerShape(15.dp)),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 5.dp)) {
                    innerTextField() }
            })

        Text(text = "Пароль:", color = colorResource(R.color.brown), fontSize = 18.sp)
        BasicTextField(
            value = viewModel.userPassword,
            onValueChange = { viewModel.UpdateUserPassword(it) },
            textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
            modifier = Modifier.height(40.dp).fillMaxWidth().background((colorResource(R.color.blue)), shape = RoundedCornerShape(15.dp)),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 5.dp)) {
                    innerTextField() }
            })
        if(!viewModel.IsThisAutorize()){
            Text(text = "Повторите пароль:", color = colorResource(R.color.brown), fontSize = 18.sp)
            BasicTextField(
                value = viewModel.userPasswordRepeat,
                onValueChange = { viewModel.UpdateUserPasswordRepeat(it) },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
                modifier = Modifier.height(40.dp).fillMaxWidth().background((colorResource(R.color.blue)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 5.dp)) {
                        innerTextField() }
                })
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Button(onClick = { viewModel.AutorizeClient(navController) },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
                Text(text = stringResource(viewModel.GetMainButtonText()), color = colorResource(R.color.brown), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SetLoginButton(viewModel: LoginViewModel){
    key(viewModel.change){
        Button(onClick =
        {
            viewModel.ChangeScreen()
        },
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(0.65f),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(colorResource(R.color.green)))
        { Text(text = stringResource(id = viewModel.GetBottomButtonText()), color = colorResource(R.color.brown), fontSize = 14.sp) }
    }
}

