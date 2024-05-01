package com.coolgirl.poctokkotlin.Common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.File

fun DecodeImage(image : String) : Bitmap{
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


