package com.coolgirl.poctokkotlin.navigate


sealed class Screen(val route: String){
    object Login : Screen("login")

    object UserPage : Screen("user_page/{user_id}"){
        fun user_id(user_id : Int): String{
            return "user_page/$user_id"
        }
    }

    object PlantPage : Screen("plant_page/{plant_id}"){
        fun plant_id(plant_id : Int): String{
            return "plant_page/$plant_id"
        }
    }
    object Register : Screen("register")

    object AddPlant : Screen("addPlant")

    object Note : Screen("note/{note_id}") {
        fun note_id(note_id: Int?): String {
            return "note/$note_id"
        }
    }
}