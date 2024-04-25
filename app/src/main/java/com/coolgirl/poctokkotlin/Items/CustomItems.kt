package com.coolgirl.poctokkotlin.Items


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                        viewModel.noteType = item.Id
                       // onItemSelected(item.Name)
                    }
                ) {
                    Text(item.Name)
                }
            }
        }
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand",
            modifier = Modifier
                .height(24.dp)
                .clickable { expanded = true }
        )
    }
}