package com.coolgirl.poctokkotlin.navigate

import android.util.Log


sealed class Screen(val route: String){
    object Login : Screen("login")

    object UserPage : Screen("user_page/{user_id}"){
        fun user_id(user_id: Int): String{
            return "user_page/$user_id"
        }
    }

    object PlantPage : Screen("plant_page/{plant_id}"){
        fun plant_id(plant_id : Int): String{
            return "plant_page/$plant_id"
        }
    }
    object Register : Screen("register")

    object EditUser : Screen("edit_user")

    object AddPlant : Screen("addPlant")

    object Settings : Screen("settings")

    object  ImageChoiceScreen : Screen("image_choice_screen/{what_it_is}"){
        fun what_it_is(what_it_is : String): String{
            return "image_choice_screen/$what_it_is"
        }
    }

    object WateringPage : Screen("watering_page")

    object Note : Screen("note/{note_id}") {
        fun note_id(note_id: Int?): String {
            return "note/$note_id"
        }
    }
}