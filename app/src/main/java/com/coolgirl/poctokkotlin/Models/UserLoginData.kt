package com.coolgirl.poctokkotlin.Models

data class UserLoginData(
    val userlogin : String,
    val userpassword : String
)

data class UserLoginDataResponse(
    val userlogin : String?,
    val userpassword : String?,
    val userid : Int,
    var userdescription : String?,
    var username : String?,
    val userimage : String?,
    var notes: List<Notes?>?,
    var plants: List<Plant?>?
)