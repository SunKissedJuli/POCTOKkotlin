package com.coolgirl.poctokkotlin.Items

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen
import java.net.URI

@Composable
fun NoteItem(noteText: String?, date: String?, plant: String?, note_id : Int, navController: NavHostController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { navController.navigate(Screen.Note.note_id(note_id)) }
        .height(150.dp)
        .background(colorResource(R.color.stone))) {
        Text(text = noteText!!, softWrap = true, modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .fillMaxHeight(0.7f), color = colorResource(R.color.brown)
        )
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
            Text(text = plant!!.toString(), modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 17.dp), color = colorResource(
                R.color.brown)
            )
            Text(text = date!!, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, end = 15.dp), color = colorResource(
                R.color.brown)
            )
        }
    }
}

@Composable
fun PlantItem(plantName : String?, plantDescription : String?, plantImage : String?, plantId : Int?, navController: NavHostController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .background(colorResource(R.color.stone)),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.avatar1),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(10.dp)
                .size(100.dp)
                .clip(CircleShape))
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)) {
            Text(text = plantName!!, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 20.dp), color = colorResource(R.color.brown), fontWeight = FontWeight.Bold)
            Text(text = plantDescription!!, softWrap = true, modifier = Modifier
                .fillMaxHeight(0.63f)
                .padding(top = 5.dp, bottom = 5.dp, start = 20.dp), color = colorResource(R.color.brown))
            Button(onClick = {navController.navigate(Screen.PlantPage.plant_id(plantId!!))},
                modifier = Modifier
                    .padding(top = 5.dp, start = 20.dp)
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.green))){
                Text(text = stringResource(R.string.user_page_go), color = colorResource(R.color.brown), fontSize = 11.sp, fontWeight = FontWeight.Medium) }
        }
    }
}

@Composable
fun PhotoItem(uri : URI){
    Image(
        painter = rememberImagePainter(uri),
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(10.dp)
            .size(100.dp)
    )
}


