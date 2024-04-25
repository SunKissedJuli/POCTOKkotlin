package com.coolgirl.poctokkotlin.Screen.PlantPage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.Common.LoadNotesStatus
import com.coolgirl.poctokkotlin.Items.*
import com.coolgirl.poctokkotlin.Items.Watering.WateringItemsViewModel
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.Screen.UserPage.*
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt


private var noteList : List<Notes?>? = null
private var photoList : List<Notes?>?= null

@Composable
fun PlantPageScreen(navController: NavHostController, plantId : Int) {

    val viewModel : PlantPageViewModel = viewModel()
    val wateringViewModel : WateringItemsViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    var loadNotesStatus by remember { mutableStateOf(LoadNotesStatus.NOT_STARTED) }

    LaunchedEffect(loadNotesStatus) {
        if (loadNotesStatus == LoadNotesStatus.NOT_STARTED) {
            coroutineScope.launch {
                viewModel.LoadPlantData(plantId)
                loadNotesStatus = LoadNotesStatus.COMPLETED
            }
        }
    }

    if (loadNotesStatus == LoadNotesStatus.COMPLETED) {
        noteList = viewModel.GetNotes()
        photoList = viewModel.GetPhotos()
        SetPlantPage(navController, viewModel, wateringViewModel)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetPlantPage(navController: NavHostController, viewModel: PlantPageViewModel, wateringViewModel : WateringItemsViewModel){
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout (
        sheetShape = RoundedCornerShape(topEnd = 65.dp, topStart = 65.dp),
        sheetState = sheetState,
        sheetContent = { BottomPanel(navController ) },
        scrimColor = colorResource(R.color.gray),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.stone))) {
                SetPlantHead(viewModel)
                SetButtonHead(viewModel)
                key(viewModel.change) {
                    when (viewModel.WhatItIs()) {
                        "shedule" ->
                            if(viewModel.plant!!.plantname!=null){
                                if(viewModel.plant!!.wateringSchedule?.schedule !=null) {
                                    Column(modifier = Modifier
                                        .fillMaxHeight(0.85f)
                                        .fillMaxWidth()
                                        .background(colorResource(R.color.blue))){
                                        Log.d("tag", "хуй PlantPage shedule = " + viewModel.plant!!.wateringSchedule!!.schedule)
                                        Shedule(viewModel.plant!!.wateringSchedule!!.schedule, wateringViewModel)
                                        AddWatering(viewModel.plant!!.plantname, wateringViewModel, true)} }
                                else{
                                    Column(modifier = Modifier
                                        .fillMaxHeight(0.85f)
                                        .fillMaxWidth()
                                        .background(colorResource(R.color.blue))) {
                                        Shedule("0000000", wateringViewModel)
                                        AddWatering(viewModel.plant!!.plantname, wateringViewModel, true)}
                                }
                            }
                        "notes" -> NoteList(viewModel, noteList, navController)
                        "photos" -> PhotoList(viewModel, photoList, navController)
                        else -> Text("sorry") }
                    if(viewModel.plant!=null){
                        BottomSheet(navController, viewModel.plant!!.userid!!, scope, sheetState) }
                    }

                }

        })
}



@Composable
fun SetPlantHead(viewModel: PlantPageViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.avatar1),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .size(130.dp)
                    .clip(CircleShape))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(25.dp, 20.dp, 0.dp, 10.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            viewModel.plant?.plantname?.let { Text(text = it, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colorResource(R.color.brown)) }
            viewModel.plant?.plantdescription?.let { Text(text = it, softWrap = true, color = colorResource(R.color.brown)) }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.stone)))
            { Text(text = stringResource(R.string.user_head_change), color = colorResource(R.color.brown), fontSize = 13.sp) }
        }
    }
}

@Composable
fun SetButtonHead(viewModel: PlantPageViewModel) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Button(onClick = { viewModel.ShowShedule() },
            colors = ButtonDefaults.buttonColors(colorResource(R.color.stone))) {
            Text(text = stringResource(R.string.button_head_shedule), color = colorResource(R.color.brown), fontSize = 13.sp) }
        Button(onClick = { viewModel.ShowPhotos() },
            colors = ButtonDefaults.buttonColors(colorResource(R.color.stone))) {
            Text(text = stringResource(R.string.button_head_photo), color = colorResource(R.color.brown), fontSize = 13.sp) }
        Button(onClick = { viewModel.ShowNotes() },
            colors = ButtonDefaults.buttonColors(colorResource(R.color.stone))) {
            Text(text = stringResource(R.string.button_head_note), color = colorResource(R.color.brown), fontSize = 13.sp) }
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
        .background(colorResource(R.color.blue))){}
}


@Composable
fun NoteList(viewModel: PlantPageViewModel, noteList: List<Notes?>?, navController: NavHostController){
    if(viewModel.WhatItIs().equals("notes")){
        var count = noteList?.size
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .background(colorResource(R.color.blue))){
            if (count != null) {
                items(count){ index ->
                    if (noteList != null) {
                        NoteItem(noteList[index]!!.notetext!!,noteList[index]!!.notedata, viewModel.plant!!.plantname, noteList[index]!!.noteid!!, navController)
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)){}
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoList(viewModel: PlantPageViewModel, noteList: List<Notes?>?, navController: NavHostController){
    if(viewModel.WhatItIs().equals("photos")){
        val columnItems : Int = ((noteList!!.size)!!.toFloat()/3).roundToInt()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight(0.86f)
                .fillMaxWidth()
                .background(colorResource(R.color.blue))) {
            items(columnItems) { columnIndex ->
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    val count = min(3, noteList.size - columnIndex * 3).coerceIn(1, 3)
                    items(count) { rowIndex ->
                        val currentIndex = columnIndex * 3 + rowIndex
                        Image(
                            painter = rememberImagePainter("http://45.154.1.94" + (noteList[currentIndex]!!.image)),
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(2.dp)
                                .size(116.dp)
                                .clickable { navController.navigate(Screen.Note.note_id(noteList[currentIndex]!!.noteid)) })
                    }
                }
            }
        }
    }
}

