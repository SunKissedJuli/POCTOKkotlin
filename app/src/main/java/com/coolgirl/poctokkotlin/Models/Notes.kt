package com.coolgirl.poctokkotlin.Models

data class Notes(
    var userid: Int?,
    var plantid: Int?,
    var image: String?,
    var notetext: String?,
    var noteid: Int?,
    var notedata: String?,
    var user: UserLoginDataResponse?
)
