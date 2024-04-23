package com.coolgirl.poctokkotlin.Common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

fun DecodeImage(image : String) : Bitmap{
  //  val gson: Gson = Gson()
//    val type: Type = object : TypeToken<Map<String, String>>() {}.type
//    val jsonMap: Map<String, String> = gson.fromJson(image, type)
 //   val imageString: String = jsonMap["Image"]!!
    val imageBytes: ByteArray = Base64.decode(image, Base64.DEFAULT )
    val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return bitmap
}

fun DecodeImageFromLocal(image : String) : Bitmap{
    val gson: Gson = Gson()
    val type: Type = object : TypeToken<Map<String, String>>() {}.type
    val jsonMap: Map<String, String> = gson.fromJson(image, type)
    val imageString: String = jsonMap["Image"]!!
    val imageBytes: ByteArray = Base64.decode(image, Base64.DEFAULT )
    val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return bitmap
}

fun EncodeImage(filePath: String): String {
    val file = File(filePath)
    val imageBytes = file.readBytes()
    val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

    return  base64Image
}

fun EncodeImageToServer(filePath: String): String {
    val file = File(filePath)
    val imageBytes = file.readBytes()
    val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

    val imageMap = mapOf("Image" to base64Image)
    val gson = Gson()
    return gson.toJson(imageMap)
}