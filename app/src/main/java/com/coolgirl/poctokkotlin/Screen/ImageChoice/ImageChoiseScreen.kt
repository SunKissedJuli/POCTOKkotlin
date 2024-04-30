package com.coolgirl.poctokkotlin.Screen.ImageChoice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen

@Composable
fun ImageChoiceScreen(navController: NavHostController, whatItIs : String){
    var viewModel : ImageChoiceViewModel = viewModel()
    viewModel.whatItIs = whatItIs
    var launcher = viewModel.OpenGalery(navController)
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.stone)),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally){
        Box(modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.4f)
            .clickable { launcher.launch("image/*") }
            .background(
                color = colorResource(R.color.blue),
                shape = RoundedCornerShape(25)
            ),
            contentAlignment = Alignment.Center) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text = stringResource(R.string.image_galery), fontSize = 30.sp, color = colorResource(R.color.brown), fontWeight = FontWeight.Bold)
                Text(text = stringResource(R.string.image_galery_description), softWrap = true, fontSize = 15.sp, color = colorResource(R.color.brown), fontWeight = FontWeight.Bold)
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.65f)
            .clickable { }
            .background(
                color = colorResource(R.color.blue),
                shape = RoundedCornerShape(25)
            ),
            contentAlignment = Alignment.Center) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text = stringResource(R.string.image_camera), fontSize = 30.sp, color = colorResource(R.color.brown), fontWeight = FontWeight.Bold)
                Text(text = stringResource(R.string.image_camera_description), softWrap = true, fontSize = 15.sp, color = colorResource(R.color.brown), fontWeight = FontWeight.Bold)
            }
        }
    }

    key(viewModel.go){
        if(viewModel.go!=null&&!viewModel.go.equals("")){
            navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))
        }
    }
}