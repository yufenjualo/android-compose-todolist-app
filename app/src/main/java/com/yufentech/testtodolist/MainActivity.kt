package com.yufentech.testtodolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yufentech.testtodolist.ui.theme.TestNewToDoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestNewToDoListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage() {
    val colorsList = mutableListOf("black","blue","yellow","blue")
    val colorsSet = mutableSetOf("black","blue","yellow","blue") // set only unique, so this length will be just 3
    val colorsMap = mapOf("black" to 1,"blue" to 2,"yellow" to 3,"blue" to 4)

    val focusManager = LocalFocusManager.current
    val myContext = LocalContext.current
    val itemList = readData(myContext)

//    val itemList = remember {
//        mutableStateListOf("Learn Kotlin","Learn Compose")
//    }

    val todoName = remember {
        mutableStateOf("")
    }

    val deleteDialogStatus = remember{
        mutableStateOf(false)
    }

    val clickedItemIndex = remember{
        mutableIntStateOf(0)
    }

    val updateDialogStatus = remember {
        mutableStateOf(false)
    }

    val clickedItem = remember {
        mutableStateOf("")
    }

    val textDialogStatus = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {

            TextField(
                textStyle = TextStyle(textAlign = TextAlign.Center),
                value = todoName.value,
                onValueChange = { todoName.value = it },
                label = { Text("Enter todo here") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.White,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .weight(7F)
                    .height(60.dp),
            )

            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = {

                    if(todoName.value.isNotEmpty()){
                        // add the new data to the list
                        itemList.add(todoName.value)

                        // write date to a file
                        writeData(itemList, myContext)

                        // reset the form
                        todoName.value= ""
                        focusManager.clearFocus()
                    }
                    else{
                        Toast.makeText(myContext, "Please enter a todo", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .weight(3F)
                    .height(60.dp),

            ) {
                Text(text = "Add", fontSize = 20.sp)
            }
        }

        LazyColumn {
            items(
                count = itemList.size,
                itemContent = { index->
                    val item = itemList[index]
                    
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(0.dp),
                    ){
                        Row(
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = item,
                                fontSize = 20.sp,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(300.dp)
                                    .clickable {
                                        clickedItem.value = item
                                        textDialogStatus.value = true
                                    }
                            )
                            Row {
                                IconButton(onClick = {
                                    updateDialogStatus.value = true
                                    clickedItemIndex.intValue = index
                                    clickedItem.value = item
                                }) {
                                    Icon(Icons.Filled.Edit, contentDescription ="edit item" , tint = Color.White)
                                }
                                IconButton(onClick = {
                                    deleteDialogStatus.value = true
                                    clickedItemIndex.intValue = index
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription ="delete item", tint = Color.White)
                                }
                            }
                        }
                    }

                }
            )

        }

        // show the delete dialog
        if(deleteDialogStatus.value){
            AlertDialog(
                onDismissRequest = {
                deleteDialogStatus.value = false },
                title = { Text(text = "Delete") },
                text = { Text(text = "Are you sure you want to delete this item?") },
                confirmButton = {
                    TextButton(onClick = {
                        itemList.removeAt(clickedItemIndex.intValue)
                        writeData(itemList, myContext)
                        deleteDialogStatus.value= false
                        Toast.makeText(myContext, "Item deleted", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteDialogStatus.value= false }) {
                        Text(text = "No")
                    }
                }
            )
        }

        // show the edit dialog
        if(updateDialogStatus.value){
            AlertDialog(
                onDismissRequest = {
                    updateDialogStatus.value = false },
                title = { Text(text = "Update") },
                text = {
                        TextField(
                            value = clickedItem.value,
                            onValueChange = { clickedItem.value = it}
                        )
                       },
                confirmButton = {
                    TextButton(onClick = {
                        itemList[clickedItemIndex.value] = clickedItem.value
                        writeData(itemList, myContext)
                        updateDialogStatus.value= false
                        Toast.makeText(myContext, "Item is updated", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { updateDialogStatus.value= false }) {
                        Text(text = "No")
                    }
                }
            )
        }

        // show the show full-todo-text dialog
        if(textDialogStatus.value){
            AlertDialog(
                onDismissRequest = {
                    textDialogStatus.value = false },
                title = { Text(text = "Todo item") },
                text = {
                    Text(text = clickedItem.value)
                },
                confirmButton = {
                    TextButton(onClick = {
                        textDialogStatus.value= false
                    }) {
                        Text(text = "Ok")
                    }
                }
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    TestNewToDoListTheme {
        MainPage()
    }
}