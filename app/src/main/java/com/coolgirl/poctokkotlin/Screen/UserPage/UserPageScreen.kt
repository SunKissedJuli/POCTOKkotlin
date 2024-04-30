package com.coolgirl.poctokkotlin.Screen.UserPage

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.R
import kotlinx.coroutines.launch
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.*
import com.coolgirl.poctokkotlin.Common.LoadNotesStatus
import com.coolgirl.poctokkotlin.Items.*
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlin.math.min
import java.util.*
import kotlin.math.roundToInt

private var noteList : List<Notes?>? = null
private var plantList : List<Plant?>?= null
private var photoList : List<Notes?>?= null

@Composable
fun UserPageScreen(navController: NavHostController, userId : Int) {
    val viewModel : UserPageViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    var loadNotesStatus by remember { mutableStateOf(LoadNotesStatus.NOT_STARTED) }

    LaunchedEffect(loadNotesStatus) {
        if (loadNotesStatus == LoadNotesStatus.NOT_STARTED) {
            coroutineScope.launch {
                viewModel.LoadNotes(userId)
                loadNotesStatus = LoadNotesStatus.COMPLETED
            }
        }
    }

    key(viewModel.DataLoaded){
        if (loadNotesStatus == LoadNotesStatus.COMPLETED) {
            noteList = viewModel.GetNotes()
            plantList = viewModel.GetPlants()
            photoList = viewModel.GetPhotos()
            SetUserPage(navController, viewModel)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun SetUserPage(navController: NavHostController, viewModel: UserPageViewModel){
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout (
        sheetShape = RoundedCornerShape(topEnd = 65.dp, topStart = 65.dp),
        sheetState = sheetState,
        sheetContent = { BottomPanel(navController) },
        scrimColor = colorResource(R.color.gray),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.stone))) {
                SetUserHead(viewModel,
                 //   viewModel.OpenGalery(),
                    navController)
                SetButtonHead(viewModel)
                key(viewModel.change){
                    when (viewModel.WhatItIs()) {
                        "plants" ->  PlantList(viewModel, plantList, navController)
                        "notes" ->  NoteList(viewModel, noteList, navController)
                        "photos" -> PhotoList(viewModel, photoList, navController)
                        else -> Text("sorry")
                    }
                }
                BottomSheet(navController, viewModel.user!!.userid, scope, sheetState )
            }
        }
    )
}

@Composable
fun SetUserHead(viewModel: UserPageViewModel,
               // launcher: ManagedActivityResultLauncher<String, Uri?>,
                navController: NavHostController) {
    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.45f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var userIcon : String? = null
            userIcon = viewModel.user?.userimage?.let { "http://45.154.1.94" + it }
            Image(
                painter = rememberImagePainter(userIcon ?: R.drawable.user_icon),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .size(130.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(Screen.ImageChoiceScreen.what_it_is("user_page"))
                       // launcher.launch("image/*")
                    }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(25.dp, 10.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top){
                viewModel.user?.username?.let {
                    Text(text = it, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colorResource(R.color.brown),
                        modifier = Modifier.padding(top=35.dp)) }
                Image(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = "settings",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(30.dp)
                        .clickable {navController.navigate(Screen.Settings.route) })
            }
             viewModel.user?.userdescription?.let {
                 Text(text = it, softWrap = true, color = colorResource(R.color.brown), modifier = Modifier.padding(end = 10.dp, top=20.dp)) }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(0.7f).padding(top=20.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.stone)))
            { Text(text = stringResource(R.string.user_head_change), color = colorResource(R.color.brown), fontSize = 13.sp) }

        }
    }
}

@Composable
fun SetButtonHead(viewModel: UserPageViewModel) {
    Row(modifier = Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
        Button(onClick = { viewModel.ShowPlants() },
            colors = ButtonDefaults.buttonColors(colorResource(R.color.stone))) {
            Text(text = stringResource(R.string.button_head_plant), color = colorResource(R.color.brown), fontSize = 13.sp) }
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
fun NoteList(viewModel: UserPageViewModel, noteList: List<Notes?>?, navController: NavHostController){
        if(viewModel.WhatItIs().equals("notes")){
            var count = noteList?.size
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .background(colorResource(R.color.blue))){
                if (count != null) {
                    items(count){ index ->
                        if (noteList != null) {
                            NoteItem(noteList[index]!!.notetext!!,noteList[index]!!.notedata, viewModel.GetPlantName(noteList[index]!!.plantid!!),noteList[index]!!.noteid!!, navController)
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
fun PhotoList(viewModel: UserPageViewModel, noteList: List<Notes?>?, navController: NavHostController){
    if(viewModel.WhatItIs().equals("photos")){
        if(noteList!=null){
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
}

@Composable
fun PlantList(viewModel: UserPageViewModel, plantList: List<Plant?>?, navController: NavHostController){
        if(viewModel.WhatItIs().equals("plants")){
            var count = plantList?.size
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .background(colorResource(R.color.blue))){
                if (count != null) {
                    items(count){ index ->
                        if (plantList != null) {
                           PlantItem(plantList[index]!!.plantname,plantList[index]!!.plantdescription,plantList[index]!!.plantimage, plantList[index]!!.plantid, navController)
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)){}
                        }
                    }
                }
            }
        }
}



