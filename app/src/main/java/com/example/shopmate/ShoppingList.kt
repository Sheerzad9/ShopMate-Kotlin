package com.example.shopmate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShoppingListApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) };
    var showModal by remember { mutableStateOf(false) };

    val gradientColors = listOf(
        Color(0xFFB3E5FC), // Light blue color
        Color(0xFF1976D2) // Dark blue color
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(0.dp, 16.dp)
                .background(
                    brush = Brush.horizontalGradient(gradientColors),
                    shape = RoundedCornerShape(50)
                )
        ){
            Button(
                onClick = { showModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(50),
                modifier = Modifier.align(Alignment.Center)
            ) {
                BasicText(text = "Add Item", style = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.SansSerif,fontWeight = FontWeight.SemiBold))
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
        ){
            items(sItems){currItem ->
                if(currItem.isEditing){
                    ShoppigListItemEditor(item = currItem,
                        onEditComplete = {
                                editedName, editedQuanity ->
                            sItems = sItems.map { item ->
                                if(item.id == currItem.id){
                                    item.copy(name = editedName, quantity = editedQuanity, isEditing = false);
                                }else {
                                    item
                                }
                            }
                        },
                        onCancel = { sItems = sItems.map {
                            it.takeIf { item -> item.id == currItem.id }
                                ?.copy(isEditing = false)
                                ?: it
                            }
                        }
                    )
                }
                ShoppingListItem(item = currItem,
                    onEditClick = { sItems = sItems.map { it.copy(isEditing = it.id==currItem.id) } },
                    onDeleteClick = { sItems = sItems.filter { it.id != currItem.id } })
            }
        }
    }

    if(showModal){
        AddItemModal(setShowModal = {showModal = it},
            handleNewItem = { sItems = sItems + it },
            itemsListSize = sItems.size)
    }
}

@Composable
fun ShoppigListItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit, onCancel: () -> Unit){
    var editedName by remember{ mutableStateOf(item.name) };
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) };
    var isEditing by remember { mutableStateOf(item.isEditing) };

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF494949))
        ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier.width(200.dp)) {
                Text("Item:");
                OutlinedTextField(value = editedName, onValueChange = {editedName = it}, singleLine = true, modifier = Modifier
                    .wrapContentSize());
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Quantity:");
                OutlinedTextField(value = editedQuantity, onValueChange = {editedQuantity = it}, singleLine = true,
                    modifier = Modifier
                        .wrapContentSize(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number));
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0460FF)),
                onClick = {
                    isEditing = false;
                    onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
                },
                modifier = Modifier
                    .width(150.dp)
                    .align(Alignment.CenterVertically)) {
                Text("Save", fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                onClick = {
                    isEditing = false;
                    onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
                },
                modifier = Modifier
                    .width(150.dp)
                    .align(Alignment.CenterVertically)) {
                Text("Cancel", fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }


}

@Composable
fun ShoppingListItem(item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit
){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Color.Cyan,
                shape = RoundedCornerShape(10.dp)
            )
    ){
        Text(fontSize = 14.sp, text = item.name, modifier = Modifier
            .padding(16.dp)
            .align(Alignment.CenterVertically));
        Text(fontSize = 14.sp, text = "Qty: ${item.quantity}", modifier = Modifier
            .padding(16.dp)
            .align(Alignment.CenterVertically));
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit icon");
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon");
            }
        }
    }
}

@Composable
fun AddItemModal(setShowModal: (show: Boolean) -> Unit, handleNewItem: (newItem:ShoppingItem) -> Unit, itemsListSize: Int){
    val context = LocalContext.current;
    var itemName by remember { mutableStateOf("") };
    var itemQuantity by remember { mutableStateOf("") };

    AlertDialog(onDismissRequest = {setShowModal(false)},
        confirmButton = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Button( colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Light green color
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        if (itemName.isBlank() || itemQuantity.isBlank()){
                            Toast.makeText(context,"Please fill all the input fields!",Toast.LENGTH_LONG).show();
                            return@Button;
                        }

                        val newItem = ShoppingItem(
                            id = itemsListSize+1,
                            name = itemName,
                            quantity = itemQuantity.trim().toInt()
                        );
                        handleNewItem(newItem);
                        setShowModal(false);
                        itemName = "";
                        itemQuantity = "";
                    } ) {
                    Text("Add", fontWeight = FontWeight.Bold, fontSize = 18.sp);
                }
                Spacer(modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp))
                Button( colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)), // Light red color
                    modifier = Modifier.width(120.dp),
                    onClick = { setShowModal(false); }) {
                    Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        },
        title = { Text("Add Shopping Item")},
        text = {
            Column {
                OutlinedTextField(value = itemName,
                    onValueChange = {itemName = it;},
                    placeholder = { Text("Item Name")},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp));

                OutlinedTextField(value = itemQuantity,
                    onValueChange = {itemQuantity = it;},
                    placeholder = { Text("Item Quantity")},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number));
            }
        }
    )
}