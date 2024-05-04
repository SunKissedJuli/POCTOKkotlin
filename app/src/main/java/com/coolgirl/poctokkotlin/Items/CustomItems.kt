package com.coolgirl.poctokkotlin.Items


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.Screen.Note.NoteViewModel
import com.coolgirl.poctokkotlin.Screen.Note.SpinnerItems

@Composable
fun SpinnerSample(
    items: List<SpinnerItems>,
    selectedItem: String,
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelectedItem by remember { mutableStateOf(selectedItem) }

    Row(modifier, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Тип записи : " + currentSelectedItem, color = colorResource(R.color.brown), modifier = Modifier.padding(10.dp))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        currentSelectedItem = item.Name
                        viewModel.noteType = item.Id })
                { Text(item.Name) }
            }
        }
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand",
            modifier = Modifier
                .height(24.dp)
                .clickable { expanded = true })
    }
}

@Composable
fun SetPlug(title : Int, description : Int, image : Int){
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .background(colorResource(R.color.blue)),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top) {
        Box(modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.7f)
            .padding(top = 10.dp)
            .clickable { }
            .background(
                color = colorResource(R.color.stone),
                shape = RoundedCornerShape(25)
            ),
            contentAlignment = Alignment.Center) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly) {
                Image(
                    painter = painterResource(image),
                    contentDescription = "image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxHeight(0.42f)
                        .fillMaxWidth())
                Text(text = stringResource(title), fontSize = 20.sp, color = colorResource(R.color.brown), fontWeight = FontWeight.Bold)
                Text(text = stringResource(description), softWrap = true, fontSize = 13.sp, color = colorResource(R.color.brown), textAlign = TextAlign.Center , fontWeight = FontWeight.Bold)
            }
        }
    }

}
