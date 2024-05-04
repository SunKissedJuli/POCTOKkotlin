package com.coolgirl.poctokkotlin.data.dto

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
    var userimage : String?,
    var notes: List<Notes?>?,
    var plants: List<Plant?>?
)