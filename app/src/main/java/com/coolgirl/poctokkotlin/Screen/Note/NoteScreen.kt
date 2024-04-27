package com.coolgirl.poctokkotlin.Screen.Note

import android.content.Context
import android.graphics.Paint.Align
import android.net.Uri
import android.util.Log
import android.widget.Spinner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.Common.DecodeImage
import com.coolgirl.poctokkotlin.Common.LoadNotesStatus
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Items.SpinnerSample
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(navController: NavHostController, noteId : Int?){
    val viewModel : NoteViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    var loadNotesStatus by remember { mutableStateOf(LoadNotesStatus.NOT_STARTED) }
    if(noteId!=0){
        LaunchedEffect(loadNotesStatus) {
            if (loadNotesStatus == LoadNotesStatus.NOT_STARTED) {
                coroutineScope.launch() {
                    if (noteId != null) { viewModel.LoadNote(noteId) }
                    loadNotesStatus = LoadNotesStatus.COMPLETED
                }
            }
        }
        if (loadNotesStatus == LoadNotesStatus.COMPLETED) {
            Log.d("tag", "хуй LoadNote note image = " + viewModel.noteImage)
            SetNoteScreen(navController,viewModel)
        }
    }else{
        SetNoteScreen(navController,viewModel)
    }

}

@Composable
fun SetNoteScreen(navController : NavHostController, viewModel: NoteViewModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.stone))) {
        SetNoteHead(navController, viewModel.GetNoteData(), viewModel, viewModel.OpenGalery())
        key(viewModel.noteImage){
            if(viewModel.noteImage!=null && !viewModel.noteImage.equals("")){
                SetNoteImage(viewModel.noteImage, viewModel)
            }
        }
        SetNoteBody(viewModel)
        SetNoteBottom(viewModel)
    }
}

@Composable
fun SetNoteHead(navController: NavHostController, noteData : String, viewModel: NoteViewModel,  launcher: ManagedActivityResultLauncher<String, Uri?>){
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.1f)
        .background(colorResource(R.color.blue)),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceAround){
        Text(text = "←", fontSize = 50.sp, color = colorResource(R.color.brown), modifier = Modifier.clickable {viewModel.SaveNote(navController) })
        Text(text = noteData, color = colorResource(R.color.brown), fontSize = 18.sp)
        Button(onClick = { launcher.launch("image/*") },
            shape = RoundedCornerShape(35.dp),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
            Text(text = "+", color = colorResource(R.color.brown), fontSize = 30.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SetNoteImage(bitmap : String?, viewModel: NoteViewModel){
    if(viewModel.IsOldImage()){
        Image(
            painter = rememberImagePainter("http://45.154.1.94" + bitmap!!),
            contentDescription = "image",
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f))
    }else{
        Image(
            painter = rememberImagePainter(bitmap?.let { DecodeImage(it) }),
            contentDescription = "image",
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.4f))
    }

}

@Composable
fun SetNoteBody(viewModel: NoteViewModel){
    Column(modifier = Modifier
        .fillMaxWidth().fillMaxHeight(0.9f)) {
        BasicTextField(value = viewModel.noteText, onValueChange = {viewModel.UpdateNoteText(it)},
            modifier = Modifier.padding(20.dp),
            textStyle = TextStyle.Default.copy(fontSize = 18.sp, color = colorResource(R.color.brown)),
            decorationBox = { innerTextField ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (viewModel.noteText.isEmpty()) {
                        Text(text = stringResource(R.string.note_hint),
                            color = colorResource(R.color.brown), modifier = Modifier.alpha(0.5f),fontSize = 18.sp) }
                    innerTextField()
                }
            })
    }
}


@Composable
fun SetNoteBottom(viewModel: NoteViewModel){
    val items = viewModel.GetSpinnerData()
    Log.d("tag", " хуй selectedItem = viewModel.GetSelectedType(items) = " + viewModel.GetSelectedType(items))
    var vool : Boolean = viewModel.GetSelectedType(items)<=items.size
    Log.d("tag", " хуй selectedItem = items.size = " + items.size)
    Log.d("tag", " хуй selectedItem = viewModel.GetSelectedType(items)<=items.size = " + vool)

    if(viewModel.GetSelectedType(items)<=items.size){
        Log.d("tag", " хуй selectedItem = items[viewModel.GetSelectedType(items)].Name = " + items[viewModel.GetSelectedType(items)].Name)

        Row(modifier = Modifier.fillMaxSize()) {
            SpinnerSample(
                items = items,
                 selectedItem = items[viewModel.GetSelectedType(items)].Name,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.blue)),
                viewModel)
        }
    }

}