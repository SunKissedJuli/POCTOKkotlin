package com.coolgirl.poctokkotlin.Common

fun getResourceNameFromDrawableString(drawableString: String): String {
    val parts = drawableString.split(".")
    return if (parts.size == 3 && parts[0] == "R" && parts[1] == "drawable") {
        parts[2]
    } else {
        ""
    }
}