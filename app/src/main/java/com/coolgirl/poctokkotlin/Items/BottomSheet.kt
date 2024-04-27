package com.coolgirl.poctokkotlin.Items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun BottomPanel(navController: NavHostController) {

  Column(modifier = Modifier
        .fillMaxWidth().fillMaxHeight(0.5f).background(colorResource(R.color.stone)), Arrangement.Center, Alignment.CenterHorizontally) {
        Text(
            text = "+",
            color = colorResource(R.color.green),
            fontSize = 65.sp,
            fontWeight = FontWeight.ExtraBold)
        Text(
            modifier = Modifier
                .padding(bottom = 30.dp),
            text = stringResource(R.string.bottom_sheet_wants),
            color = colorResource(R.color.brown),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold)
        Button(onClick = { navController.navigate(Screen.Note.note_id(0)) },
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth(0.7f)
                .height(45.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
            Text(text = stringResource(R.string.bottom_sheet_note),
                color = colorResource(R.color.brown), fontSize = 17.sp, fontWeight = FontWeight.Bold
            ) }
        Button(onClick = { navController.navigate(Screen.AddPlant.route) },
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth(0.7f)
                .height(45.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
            Text(text = stringResource(R.string.bottom_sheet_plant),
                color = colorResource(R.color.brown), fontSize = 17.sp, fontWeight = FontWeight.Bold) }
        Button(onClick = {  },
            modifier = Modifier
                .padding(bottom = 25.dp)
                .fillMaxWidth(0.7f)
                .height(45.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
            Text(text = stringResource(R.string.bottom_sheet_poliv),
                color = colorResource(R.color.brown), fontSize = 17.sp, fontWeight = FontWeight.Bold) }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(navController: NavHostController, userId: Int, scope: CoroutineScope, sheetState: ModalBottomSheetState) {
    Row(modifier = Modifier.fillMaxSize(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.drop),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(5.dp)
                .size(50.dp)
                .clip(CircleShape))
        Text(
            text = "ᐩ",
            modifier = Modifier
                .align(Alignment.Top)
                .clickable {
                    scope.launch { sheetState.show() }
                },
            color = colorResource(R.color.green),
            fontSize = 85.sp,
            fontWeight = FontWeight.ExtraBold)

        var userIcon : String? = null
        userIcon = GetUser()?.userimage?.let { "http://45.154.1.94" + it }
        Image(
            painter = rememberImagePainter(userIcon ?: R.drawable.user_icon),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clickable { navController.navigate(Screen.UserPage.user_id(userId)) }
                .padding(5.dp)
                .size(55.dp)
                .clip(CircleShape))
    }
}