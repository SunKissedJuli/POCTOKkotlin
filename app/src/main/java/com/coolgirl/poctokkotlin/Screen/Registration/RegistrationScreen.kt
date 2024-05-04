package com.coolgirl.poctokkotlin.Screen

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.coolgirl.poctokkotlin.commons.DecodeImage
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.Screen.Registration.RegistrationViewModel
import com.coolgirl.poctokkotlin.Screen.UserPage.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegistrationScreen(navController: NavController){
    var viewModel : RegistrationViewModel = viewModel()
    viewModel.sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    viewModel.scope = rememberCoroutineScope()
    ModalBottomSheetLayout (
        sheetShape = RoundedCornerShape(topEnd = 65.dp, topStart = 65.dp),
        sheetState =  viewModel.sheetState!!,
        sheetContent = { UserImageBottomSheet(viewModel) },
        scrimColor = colorResource(R.color.gray),
        content = {

            SetRegistrationScreen(navController, viewModel)

        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetRegistrationScreen(navController: NavController, viewModel: RegistrationViewModel){
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.blue)), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {

        key(viewModel.userImage){
            if(viewModel.userImage!=null && !viewModel.userImage.equals("")){
                Image( painter = rememberImagePainter(viewModel.userImage?.let { DecodeImage(it) }),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable {  viewModel.scope!!.launch {  viewModel.sheetState!!.show() } }
                        .padding(top = 50.dp)
                        .size(150.dp)
                        .clip(CircleShape))
            }else{
                Image(painter = painterResource(R.drawable.blueimage),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable {  viewModel.scope!!.launch {  viewModel.sheetState!!.show() } }
                        .padding(top = 50.dp)
                        .size(150.dp)
                        .clip(CircleShape))
            }
        }

        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.register_nick),
                color = colorResource(R.color.brown),
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp))
            BasicTextField(
                value = viewModel.userNickname,
                onValueChange = { viewModel.UpdateUserNickname(it) },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .background((colorResource(R.color.white)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)) {
                        innerTextField() }
                })
        }
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = stringResource(R.string.register_about),
                color = colorResource(R.color.brown),
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp))
            BasicTextField(
                value = viewModel.userDescription,
                onValueChange = { viewModel.UpdateUserDescription(it) },
                textStyle = TextStyle.Default.copy(fontSize = 18.sp, textAlign = TextAlign.Justify),
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
                    .background((colorResource(R.color.white)), shape = RoundedCornerShape(15.dp)),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 5.dp)) {
                        innerTextField() }
                })
        }

        Button(onClick = { viewModel.Registration(navController) },
            modifier = Modifier
                .padding(top = 20.dp, bottom = 40.dp)
                .fillMaxWidth(0.6f),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.green))) {
            Text(text = stringResource(R.string.register_go), color = colorResource(R.color.brown), fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserImageBottomSheet(viewModel: RegistrationViewModel) {
    key( viewModel.fileName.value){
        if( viewModel.fileName.value!=0){
            viewModel.GetFileFromDrawable( viewModel.fileName.value!!)
        }
    }

   Column(modifier = Modifier
       .fillMaxWidth()
       .fillMaxHeight(0.5f)
       .background(colorResource(R.color.stone)),
   verticalArrangement = Arrangement.SpaceAround,
   horizontalAlignment = Alignment.CenterHorizontally) {
       Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
           ImageItemForBottomSheet(R.drawable.avatar11, viewModel)
           ImageItemForBottomSheet(R.drawable.avatar11, viewModel)
           ImageItemForBottomSheet(R.drawable.avatar2, viewModel)
       }
       Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
           ImageItemForBottomSheet(R.drawable.avatar3, viewModel)
           ImageItemForBottomSheet(R.drawable.avatar4, viewModel)
           ImageItemForBottomSheet(R.drawable.avatar5, viewModel)
       }
       Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
           ImageItemForBottomSheet(R.drawable.avatar6, viewModel)
           ImageItemForBottomSheet(R.drawable.universal1, viewModel)
           ImageItemForBottomSheet(R.drawable.univarsal2, viewModel)
       }
   }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageItemForBottomSheet(image : kotlin.Int, viewModel: RegistrationViewModel){
    Image(
        painter = painterResource(image),
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(15.dp)
            .size(90.dp)
            .clip(CircleShape)
            .clickable {  viewModel.fileName.value = image
                viewModel.scope!!.launch {  viewModel.sheetState!!.hide() }}
            .border(2.dp, colorResource(R.color.brown), CircleShape))
}

