package com.coolgirl.poctokkotlin.Screen.WateringPage

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.poctokkotlin.Common.LoadNotesStatus
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Items.BottomPanel
import com.coolgirl.poctokkotlin.Items.BottomSheet
import com.coolgirl.poctokkotlin.Items.HistoryItem
import com.coolgirl.poctokkotlin.Items.Shedule
import com.coolgirl.poctokkotlin.Items.Watering.WateringItemsViewModel
import com.coolgirl.poctokkotlin.Models.WateringHistory
import com.coolgirl.poctokkotlin.Models.WateringSchedule
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.Screen.PlantPage.*
import kotlinx.coroutines.launch

private var wateringView : WateringItemsViewModel? = null

@Composable
fun WateringPageScreen(navController: NavHostController){
    var viewModel : WateringPageViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    var loadNotesStatus by remember { mutableStateOf(LoadNotesStatus.NOT_STARTED) }

    LaunchedEffect(loadNotesStatus) {
        if (loadNotesStatus == LoadNotesStatus.NOT_STARTED) {
            coroutineScope.launch {
                viewModel.GetData()
                loadNotesStatus = LoadNotesStatus.COMPLETED
            }
        }
    }

    key(viewModel.DataLoaded){
        if (loadNotesStatus == LoadNotesStatus.COMPLETED) {
            wateringView = viewModel()
            SetWateringPageScreen(viewModel, navController)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SetWateringPageScreen(viewModel: WateringPageViewModel, navController: NavHostController){

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout (
        sheetShape = RoundedCornerShape(topEnd = 65.dp, topStart = 65.dp),
        sheetState = sheetState,
        sheetContent = { BottomPanel(navController) },
        scrimColor = colorResource(R.color.gray),
        content = {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.stone))) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.905f)
                    .verticalScroll(scrollState)
                    .background(colorResource(R.color.blue)),
                verticalArrangement = Arrangement.Top) {
                    ExpandableCard("Графики полива", viewModel)
                    ExpandableCard("Истории полива", viewModel)
                }
                BottomSheet(navController, GetUser()!!.userid, scope, sheetState)
            }

        }
    )
}

@Composable
fun SetNotifications(){

}

@Composable
fun SheduleList(sheduleList : List<WateringSchedule?>?, viewModel: WateringPageViewModel){
    if(sheduleList!=null){
        for(item in sheduleList){
            Shedule(item!!.schedule ?: "0000000", viewModel.GetPlantName(item.plantid!!), item.plantid, wateringView!!, false)
        }
    }
}

@Composable
fun HistoryList(historyList : List<WateringHistory?>?, viewModel: WateringPageViewModel){
    if(historyList!=null){
        for(item in historyList){
          HistoryItem(viewModel.GetPlantName(item!!.plantid!!), item.date , item.countofmililiters.toString())
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ExpandableCard(
    title: String,
    viewModel: WateringPageViewModel
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = { expandedState = !expandedState }) {
        Column(modifier = Modifier
                .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(70.dp)
                    .background(colorResource(R.color.stone))) {
                Text(
                    modifier = Modifier
                        .weight(6f)
                        .padding(start = 20.dp),
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.brown),
                    overflow = TextOverflow.Ellipsis)
                IconButton(
                    modifier = Modifier
                        .weight(2f)
                        .rotate(rotationState),
                    onClick = { expandedState = !expandedState }) {
                    Icon(
                        painter = painterResource(R.drawable.down_button),
                        contentDescription = "image") }
            }
            if (expandedState) {
                if(title.equals("Графики полива")) {
                    SheduleList(viewModel.GetShedule(), viewModel)
                }else {
                    HistoryList(viewModel.GetHistory(), viewModel)
                }
            }
        }
    }
}

