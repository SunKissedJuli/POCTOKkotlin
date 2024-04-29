package com.coolgirl.poctokkotlin.Models

data class WateringResponse (

    var wateringHistories: List<WateringHistory?>?,

    var wateringSchedule: List<WateringSchedule?>?
)