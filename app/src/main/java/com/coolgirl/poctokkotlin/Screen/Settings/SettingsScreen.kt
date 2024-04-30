package com.coolgirl.poctokkotlin.Screen.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen

@Composable
fun SettingsScreen(navController: NavHostController){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.stone))) {
        SetSettingsHeader(navController)
    }
}

@Composable
fun SetSettingsHeader(navController : NavHostController){
    Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(colorResource(R.color.blue)),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start){
        Text(text = "←",
            fontSize = 50.sp,
            color = colorResource(R.color.brown),
            modifier = Modifier
                .clickable { navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid)) }
                .padding(end = 30.dp, start = 10.dp))
        Text(text = stringResource(R.string.settings),
            color = colorResource(R.color.brown),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold)
    }
}