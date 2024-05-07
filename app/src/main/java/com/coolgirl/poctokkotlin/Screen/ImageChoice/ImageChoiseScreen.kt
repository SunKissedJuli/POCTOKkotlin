package com.coolgirl.poctokkotlin.Screen.ImageChoice

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageChoiceScreen(navController: NavHostController, whatItIs : String) {
    var viewModel: ImageChoiceViewModel = viewModel()
   // viewModel.fileName.value=0
    viewModel.whatItIs = whatItIs
    viewModel.sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    viewModel.scope = rememberCoroutineScope()
    ModalBottomSheetLayout (
        sheetShape = RoundedCornerShape(topEnd = 65.dp, topStart = 65.dp),
        sheetState =  viewModel.sheetState!!,
        sheetContent = { ImageBottomSheet(viewModel, navController) },
        scrimColor = colorResource(R.color.gray),
        content = {
            SetImageChoiceScreen(navController, viewModel)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetImageChoiceScreen(
    navController: NavHostController, viewModel: ImageChoiceViewModel, launcher: ManagedActivityResultLauncher<String, Uri?> = viewModel.OpenGalery(navController)){
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(colorResource(R.color.stone)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start){
            Text(text = "←",
                fontSize = 50.sp,
                color = colorResource(R.color.brown),
                modifier = Modifier
                    .clickable {
                       viewModel.GoBack(navController)
                    }
                    .padding(end = 30.dp, start = 10.dp))
            Text(text = "Выбор изображения",
                color = colorResource(R.color.brown),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.blue)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.4f)
                .clickable { launcher.launch("image/*") }
                .background(color = colorResource(R.color.stone), shape = RoundedCornerShape(25)),
                contentAlignment = Alignment.Center) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    Image(painter = painterResource(R.drawable.photo_plug),
                        contentDescription = "image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight(0.42f)
                            .fillMaxWidth())
                    Text(text = stringResource(R.string.image_galery),
                        fontSize = 20.sp,
                        color = colorResource(R.color.brown),
                        fontWeight = FontWeight.Bold)
                    Text(text = stringResource(R.string.image_galery_description),
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        fontSize = 13.sp,
                        color = colorResource(R.color.brown),
                        fontWeight = FontWeight.Bold)
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.65f)
                .clickable { viewModel.scope!!.launch { viewModel.sheetState!!.show() } }
                .background(color = colorResource(R.color.stone), shape = RoundedCornerShape(25)),
                contentAlignment = Alignment.Center) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly) {
                    Image(painter = painterResource(R.drawable.photo_plug),
                        contentDescription = "image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight(0.42f)
                            .fillMaxWidth())
                    Text(text = "Наши предложения",
                        fontSize = 20.sp,
                        color = colorResource(R.color.brown),
                        fontWeight = FontWeight.Bold)
                    Text(text = "Вы можете выбрать изображение из предложенных",
                        softWrap = true,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = colorResource(R.color.brown),
                        fontWeight = FontWeight.Bold)
                }
            }
            key(viewModel.go) {
                if (viewModel.go != null && !viewModel.go.equals("")) {
                    navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid)) }
            }
        }
    }
}

@Composable
fun ImageBottomSheet(viewModel: ImageChoiceViewModel, navController: NavHostController) {
    key( viewModel.fileName.value){
        if( viewModel.fileName.value!=0){
            viewModel.GetFileFromDrawable( viewModel.fileName.value!!, navController)
        }
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.7f)
        .background(colorResource(R.color.stone)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            ImageForBottomSheet(R.drawable.avatar11, viewModel)
            ImageForBottomSheet(R.drawable.avatar2, viewModel)
            ImageForBottomSheet(R.drawable.avatar3, viewModel)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            ImageForBottomSheet(R.drawable.avatar4, viewModel)
            ImageForBottomSheet(R.drawable.avatar5, viewModel)
            ImageForBottomSheet(R.drawable.avatar6, viewModel)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            ImageForBottomSheet(R.drawable.universal1, viewModel)
            ImageForBottomSheet(R.drawable.univarsal2, viewModel)
            ImageForBottomSheet(R.drawable.plant_icon, viewModel)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            ImageForBottomSheet(R.drawable.plant1, viewModel)
            ImageForBottomSheet(R.drawable.plant2, viewModel)
            ImageForBottomSheet(R.drawable.plant3, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageForBottomSheet(image : kotlin.Int, viewModel : ImageChoiceViewModel){
    Image(
        painter = painterResource(image),
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(15.dp)
            .size(90.dp)
            .clip(CircleShape)
            .clickable {
                viewModel.fileName.value = image
                viewModel.scope!!.launch { viewModel.sheetState!!.hide() }
            }
            .border(2.dp, colorResource(R.color.brown), CircleShape))
}



