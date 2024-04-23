package com.coolgirl.poctokkotlin.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.Screen.Registration.RegistrationViewModel

@Composable
fun RegistrationScreen(navController: NavController){
    var viewModel : RegistrationViewModel = viewModel()
    SetRegistrationScreen(navController, viewModel)
}

@Composable
fun SetRegistrationScreen(navController: NavController, viewModel: RegistrationViewModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.blue)), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.blueimage),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 50.dp)
                .size(150.dp)
                .clip(CircleShape))
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.register_nick),
                color = colorResource(R.color.brown),
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp))
            BasicTextField(
                value = viewModel.userNickname,
                onValueChange = { viewModel.UpdateUserNickname(it) },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background((colorResource(R.color.white)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)) {
                        innerTextField() }
                })
        }
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.register_about),
                color = colorResource(R.color.brown),
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp))
            BasicTextField(
                value = viewModel.userDescription,
                onValueChange = { viewModel.UpdateUserDescription(it) },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
                    .background((colorResource(R.color.white)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)) {
                        innerTextField() }
                })
        }

        Button(onClick = { viewModel.Registration(navController) },
            modifier = Modifier
                .padding(top = 20.dp, bottom = 40.dp)
                .fillMaxWidth(0.6f),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
            Text(text = stringResource(R.string.register_go), color = colorResource(R.color.brown), fontSize = 14.sp)
        }
    }
}