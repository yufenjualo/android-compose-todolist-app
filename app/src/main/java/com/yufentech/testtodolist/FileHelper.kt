package com.yufentech.testtodolist

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todolist.dat" // can be .bat OR .txt file

fun writeData(items: SnapshotStateList<String>, context: Context){
    val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
    val oas = ObjectOutputStream(fos)

    val itemList = ArrayList<String>() // this is same as mutableListOf, this create empty array
    itemList.addAll(items)

    // can try use spread operator too like this:
    // val itemList = mutableListOf(*items.toTypedArray())
    // but we use ArrayList<String>() to ensure it is serializable

    oas.writeObject(itemList)
    oas.close()
}

fun readData(context: Context): SnapshotStateList<String>{
    var itemList: ArrayList<String>

    try {
        val fis = context.openFileInput(FILE_NAME)
        val ois = ObjectInputStream(fis)
        itemList = ois.readObject() as ArrayList<String>
        ois.close()
    } catch (e: FileNotFoundException){
        itemList = ArrayList()
    }

    val items = SnapshotStateList<String>()
    items.addAll(itemList)
    return items
}