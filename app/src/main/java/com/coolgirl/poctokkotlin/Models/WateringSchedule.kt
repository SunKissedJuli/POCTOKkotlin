package com.coolgirl.poctokkotlin.Models

data class WateringSchedule(
    var plantid: Int?,
    var userid: Int?,
    var schedule: String?,
    var id: Int?,
    var plant: Plant?,
)

data class WateringScheduleAdd(
    var plantid: Int?,
    var userid: Int?,
    var schedule: String?
)
