package com.coolgirl.poctokkotlin.Screen.AddPlant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.commons.DecodeImage
import com.coolgirl.poctokkotlin.Items.Shedule
import com.coolgirl.poctokkotlin.Items.Watering.WateringItemsViewModel
import com.coolgirl.poctokkotlin.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddPlantScreen(navController: NavController){
    var viewModel : AddPlantViewModel = viewModel()
    viewModel.CreatePlant()
    viewModel.sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    viewModel.scope = rememberCoroutineScope()
    ModalBottomSheetLayout (
        sheetShape = RoundedCornerShape(topEnd = 65.dp, topStart = 65.dp),
        sheetState = viewModel.sheetState!!,
        sheetContent = { PlantImageBottomSheet(viewModel) },
        scrimColor = colorResource(R.color.gray),
        content = {
            SetAppPlantScreen(navController, viewModel)
        }
    )
}

@Composable
fun SetAppPlantScreen(navController: NavController, viewModel: AddPlantViewModel){
    var sheduleView : WateringItemsViewModel = viewModel()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.blue)), verticalArrangement = Arrangement.SpaceBetween) {
        AddPlantHead(navController, viewModel)
        Shedule(sheduleData = "0000000", viewModel.plantNickname, 0, viewModel = sheduleView, true)
      //  AddWatering(plantName = viewModel.plantNickname, viewModel = sheduleView, isAdding = true)
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

@OptIn(ExperimentalMaterialApi::class)
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
            key(viewModel.plantImage) {
                if (viewModel.plantImage != null && !viewModel.plantImage.equals("")) {
                    Image(
                        painter = rememberImagePainter(viewModel.plantImage?.let { DecodeImage(it)} ),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(120.dp)
                            .clip(CircleShape))
                }else{
                    Image(
                        painter = painterResource(R.drawable.plant_icon),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(120.dp)
                            .clip(CircleShape))
                }
            }
            Button(onClick = {  viewModel.scope!!.launch { viewModel.sheetState!!.show() }},
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantImageBottomSheet(viewModel: AddPlantViewModel) {
    key(viewModel.fileName.value){
        if(viewModel.fileName.value!=0){
            viewModel.GetFileFromDrawable(viewModel.fileName.value!!)
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.5f)
        .background(colorResource(R.color.stone)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            ImageItemForPlantBottomSheet(R.drawable.plant1, viewModel)
            ImageItemForPlantBottomSheet(R.drawable.plant1, viewModel)
            ImageItemForPlantBottomSheet(R.drawable.plant2, viewModel)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            ImageItemForPlantBottomSheet(R.drawable.plant3, viewModel)
            ImageItemForPlantBottomSheet(R.drawable.plant_icon, viewModel)
            ImageItemForPlantBottomSheet(R.drawable.universal1, viewModel)
        }
        Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp), horizontalArrangement = Arrangement.Start){
            ImageItemForPlantBottomSheet(R.drawable.univarsal2, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageItemForPlantBottomSheet(image : kotlin.Int, viewModel: AddPlantViewModel){
    Image(
        painter = painterResource(image),
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(15.dp)
            .size(90.dp)
            .clip(CircleShape)
            .clickable { viewModel.fileName.value = image
                viewModel.scope!!.launch { viewModel.sheetState!!.hide() }}
            .border(2.dp, colorResource(R.color.brown), CircleShape))
}