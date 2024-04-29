package com.coolgirl.poctokkotlin.Models

data class WateringHistory(
    var userid: Int?,
    var plantid: Int?,
    var date: String?,
    var countofmililiters: Int?,
    var wateringid: Int?,
    var plant: Plant?
)

data class WateringHistoryAdd(
    var userid: Int?,
    var plantid: Int?,
    var date: String?,
    var countofmililiters: Int?
)
