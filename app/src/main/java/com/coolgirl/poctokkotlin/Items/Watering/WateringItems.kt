package com.coolgirl.poctokkotlin.Items

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coolgirl.poctokkotlin.Items.Watering.WateringItemsViewModel
import com.coolgirl.poctokkotlin.R

@Composable
fun Shedule(sheduleData : String?, viewModel: WateringItemsViewModel){
    Log.d("tag", "хуй PlantPage sheduleData = " + sheduleData)
    if (sheduleData != null) {
        viewModel.SetShedule(sheduleData)
        Log.d("tag", "хуй PlantPage sheduleData !=null = " + sheduleData)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(175.dp)
        .background(colorResource(R.color.blue)),
        verticalArrangement = Arrangement.Center) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(colorResource(R.color.stone))){
            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.75f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start) {
                Text(stringResource(R.string.shedule_name),
                    color = colorResource(R.color.brown),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                    .padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top){
                    LazyRow(){
                        items(7) { index ->
                            val state: Char = sheduleData!!.toCharArray().get(index)
                            val stateBoolean = state == '1'
                            SetRadioButton(stateBoolean, index, viewModel)
                            Log.d("tag", "хуй PlantPage stateBoolean = " + stateBoolean)
                        }
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(colorResource(R.color.blue))){}
                Row(modifier = Modifier
                    .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround){
                    Button(onClick = { viewModel.UpdateShedule() },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.75f),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
                        Text(text = stringResource(R.string.small_change), color = colorResource(R.color.brown), fontSize = 12.sp)
                    }
                    //тут колокольчик
                }
            }
            Column(modifier = Modifier
                .fillMaxHeight()
                .width(5.dp)
                .background(colorResource(R.color.blue))) {}
            Column(modifier = Modifier
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Image(painter = painterResource(R.drawable.drop), contentDescription = "image", modifier = Modifier.size(50.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)) {
                    BasicTextField(value = viewModel.sheduleMl, onValueChange = {viewModel.UpdateSheduleMl(it)},
                        textStyle = TextStyle.Default.copy(color = colorResource(R.color.brown)),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(45.dp)
                            .padding(start = 5.dp, end = 5.dp))
                    Text(text = stringResource(R.string.watering_ml), fontSize = 14.sp, color = colorResource(R.color.brown))
                }
            }
        }
    }
}

@Composable
fun SetRadioButton(state: Boolean, i: Int, viewModel : WateringItemsViewModel){
    var day : String = ""
    var buttonsState by remember { mutableStateOf(state) }
    when(i){
        0 -> day = "ПН"
        1 -> day = "ВТ"
        2 -> day = "СР"
        3 -> day = "ЧТ"
        4 -> day = "ПТ"
        5 -> day = "Сб"
        6 -> day = "ВС"
    }

    Column(modifier = Modifier.width(22.dp)) {
        RadioButton(selected = buttonsState, onClick = {  buttonsState = !buttonsState
            viewModel.UpdateLocalShedule(i, buttonsState) })
        Text(text = day, fontSize = 12.sp, color = colorResource(R.color.brown), fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 3.dp))
    }
}

@Composable
fun AddWatering(plantName : String?, viewModel: WateringItemsViewModel, isAdding : Boolean){
    var name : Int = 0
    if(isAdding){ name = R.string.add_watering }
    else{ name = R.string.last_watering }

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(175.dp)
        .background(colorResource(R.color.blue)),
    verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .background(colorResource(R.color.stone))){
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start) {
                    Text(stringResource(name),
                        color = colorResource(R.color.brown),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp))
                    Text(text = (stringResource(R.string.small_watering) + " " + plantName!!),
                        color = colorResource(R.color.brown),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 5.dp, start = 10.dp))
                    Text(text = (stringResource(R.string.add_data) + " " + plantName!!),
                        color = colorResource(R.color.brown),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 5.dp, start = 10.dp))
                    Button(onClick = {  },
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(0.58f)
                            .padding(start = 30.dp, bottom = 5.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
                        Text(text = stringResource(R.string.small_add), color = colorResource(R.color.brown), fontSize = 12.sp)
                    }

                }
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(colorResource(R.color.blue))) {}
                Column(modifier = Modifier
                    .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Image(painter = painterResource(R.drawable.drop), contentDescription = "image", modifier = Modifier.size(50.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)) {
                        BasicTextField(value = viewModel.sheduleMl, onValueChange = {viewModel.UpdateSheduleMl(it)},
                            textStyle = TextStyle.Default.copy(color = colorResource(R.color.brown)),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(45.dp)
                                .padding(start = 5.dp, end = 5.dp))
                        Text(text = stringResource(R.string.watering_ml), fontSize = 14.sp, color = colorResource(R.color.brown))
                    }
                }
            }
    }

}