package com.coolgirl.poctokkotlin.Screen.AddPlant

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.Items.AddWatering
import com.coolgirl.poctokkotlin.Items.Shedule
import com.coolgirl.poctokkotlin.Items.Watering.WateringItemsViewModel
import com.coolgirl.poctokkotlin.R

@Composable
fun AddPlantScreen(navController: NavController){
    var viewModel : AddPlantViewModel = viewModel()
    viewModel.CreatePlant()
    SetAppPlantScreen(navController, viewModel)
}

@Composable
fun SetAppPlantScreen(navController: NavController, viewModel: AddPlantViewModel){
    var sheduleView : WateringItemsViewModel = viewModel()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.blue)), verticalArrangement = Arrangement.SpaceBetween) {
        AddPlantHead(navController, viewModel)
        Shedule(sheduleData = "0000000", viewModel = sheduleView)
        AddWatering(plantName = viewModel.plantNickname, viewModel = sheduleView, isAdding = true)
        Row(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(colorResource(R.color.stone)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { viewModel.Save(navController) },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp, start = 2.dp)
                    .fillMaxWidth(0.7f),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
                Text(text = stringResource(R.string.addplant_save),
                    color = colorResource(R.color.brown),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AddPlantHead(navController: NavController, viewModel: AddPlantViewModel){
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.32f)
        .background(colorResource(R.color.stone))) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.49f)
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.plant_icon),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(120.dp)
                    .clip(CircleShape))
            Button(onClick = {  },
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 20.dp, start = 2.dp)
                    .fillMaxWidth(0.90f),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
                Text(text = stringResource(R.string.addplant_changephoto), color = colorResource(R.color.brown), fontSize = 12.sp, letterSpacing = 0.1.sp)
            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, end = 15.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            BasicTextField(
                value = viewModel.plantNickname,
                onValueChange = { viewModel.UpdatePlantNickname(it) },
                textStyle = TextStyle.Default.copy(fontSize = 14.sp, textAlign = TextAlign.Justify),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background((colorResource(R.color.blue)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)) {
                        if (viewModel.plantNickname.isEmpty()) {
                            Text(text = stringResource(R.string.addplant_name),
                                color = colorResource(R.color.brown), modifier = Modifier.alpha(0.5f),fontSize = 14.sp) }
                        innerTextField() }
                })
            BasicTextField(
                value = viewModel.plantDescription,
                onValueChange = { viewModel.UpdatePlantDescription(it) },
                textStyle = TextStyle.Default.copy(fontSize = 14.sp, textAlign = TextAlign.Justify),
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background((colorResource(R.color.blue)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)) {
                        if (viewModel.plantDescription.isEmpty()) {
                            Text(text = stringResource(R.string.addplant_description),
                                color = colorResource(R.color.brown), modifier = Modifier.alpha(0.5f),fontSize = 14.sp) }
                        innerTextField() }
                })
        }
    }
}