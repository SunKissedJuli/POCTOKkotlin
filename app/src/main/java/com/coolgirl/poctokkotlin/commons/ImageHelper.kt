package com.coolgirl.poctokkotlin.commons

import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun getResourceNameFromDrawableString(drawableString: String): String {
    val parts = drawableString.split(".")
    return if (parts.size == 3 && parts[0] == "R" && parts[1] == "drawable") {
        parts[2]
    } else {
        ""
    }
}

fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024) // buffer size
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
}