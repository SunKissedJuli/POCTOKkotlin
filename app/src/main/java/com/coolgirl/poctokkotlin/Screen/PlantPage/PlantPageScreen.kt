package com.coolgirl.poctokkotlin.Screen.PlantPage

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.Common.LoadNotesStatus
import com.coolgirl.poctokkotlin.GetPlant
import com.coolgirl.poctokkotlin.Items.*
import com.coolgirl.poctokkotlin.Items.Watering.WateringItemsViewModel
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.WateringHistory
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.Screen.UserPage.*
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt


private var noteList : List<Notes?>? = null
private var photoList : List<Notes?>?= null
private var historyList : List<WateringHistory?>?= null

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
    key(viewModel.dataLoaded) {
        if (loadNotesStatus == LoadNotesStatus.COMPLETED) {
            noteList = viewModel.GetNotes()
            photoList = viewModel.GetPhotos()
            historyList = viewModel.GetHistoryList()
            SetPlantPage(navController, viewModel, wateringViewModel)
        }
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
                        "shedule" -> PlantWatering(viewModel, wateringViewModel)
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
            var plantIcon : String? = null
            plantIcon = GetPlant()?.plantimage?.let { "http://45.154.1.94" + it }
            Image(
                painter = rememberImagePainter(plantIcon ?: R.drawable.plant_icon),
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
fun PlantWatering(viewModel: PlantPageViewModel, wateringViewModel : WateringItemsViewModel){
    val scrollState = rememberScrollState()
    if(viewModel.plant!=null){
        if(viewModel.plant!!.plantname!=null){
            Column(modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .background(colorResource(R.color.blue))){
                Shedule(viewModel.plant!!.wateringSchedule?.schedule ?: "0000000", viewModel.plant!!.plantname, viewModel.plant!!.plantid, wateringViewModel, false)
                AddWatering(viewModel.plant!!.plantname, wateringViewModel, true)
                HistoryList(historyList, viewModel)
            }
        }
    }
}

@Composable
fun NoteList(viewModel: PlantPageViewModel, noteList: List<Notes?>?, navController: NavHostController){
    if(viewModel.WhatItIs().equals("notes")){
        var count = noteList?.size
        if (count != null&&count!=0) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .background(colorResource(R.color.blue))){
                items(count){ index ->
                    if (noteList != null) {
                        NoteItem(noteList[index]!!.notetext!!,noteList[index]!!.notedata, viewModel.plant!!.plantname, noteList[index]!!.noteid!!, navController)
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)){}
                    }
                }
            }
        }else { SetPlug(R.string.plug_note, R.string.plug_note_description, R.drawable.note_plug) }
    }
}

@Composable
fun PhotoList(viewModel: PlantPageViewModel, photoList: List<Notes?>?, navController: NavHostController) {
    if (viewModel.WhatItIs().equals("photos")) {
        if (photoList != null && photoList.isNotEmpty()) {
            val columnItems: Int = ((photoList.size).toFloat() / 3).roundToInt()+1
            Log.d("tag", "хуй columnItems = " + columnItems)
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight(0.86f)
                    .fillMaxWidth()
                    .background(colorResource(R.color.blue))
            ) {
                items(columnItems) { columnIndex ->
                    Log.d("tag", "хуй columnIndex = " + columnIndex)
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start) {
                        for (rowIndex in 0 until min(3, photoList.size - columnIndex * 3)) {
                            val currentIndex = columnIndex * 3 + rowIndex
                            Image(
                                painter = rememberImagePainter("http://45.154.1.94" + (photoList[currentIndex]!!.image)),
                                contentDescription = "image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(116.dp)
                                    .clickable { navController.navigate(Screen.Note.note_id(photoList[currentIndex]!!.noteid)) })
                        }
                    }
                }
            }
        } else {
            SetPlug(R.string.plug_photo, R.string.plug_photo_description, R.drawable.photo_plug)
        }
    }
}

@Composable
fun HistoryList(historyList : List<WateringHistory?>?, viewModel: PlantPageViewModel){
   Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.blue))){
       if (historyList != null) {
           if (historyList.size != null) {
               for(item in historyList){
                   HistoryItem(GetPlant()!!.plantname, item!!.date, item!!.countofmililiters.toString())
                   Row(modifier = Modifier
                       .fillMaxWidth()
                       .height(20.dp)){}
               }
           }
       }
    }
}

