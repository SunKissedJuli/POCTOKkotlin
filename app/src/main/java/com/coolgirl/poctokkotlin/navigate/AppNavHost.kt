package com.coolgirl.poctokkotlin.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.coolgirl.poctokkotlin.Screen.AddPlant.AddPlantScreen
import com.coolgirl.poctokkotlin.Screen.Login.LoginScreen
import com.coolgirl.poctokkotlin.Screen.Note.NoteScreen
import com.coolgirl.poctokkotlin.Screen.PlantPage.PlantPageScreen
import com.coolgirl.poctokkotlin.Screen.RegistrationScreen
import com.coolgirl.poctokkotlin.Screen.UserPage.UserPageScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.UserPage.route,
            arguments = listOf(navArgument("user_id"){
                type = NavType.IntType
            })){ backStackEntry ->
            val user_id : Int = backStackEntry.arguments?.getInt("user_id")!!
            UserPageScreen(navController,user_id)
        }

        composable(Screen.PlantPage.route,
            arguments = listOf(navArgument("plant_id"){
                type = NavType.IntType
                defaultValue = 0
            })){
            val plant_id : Int = it.arguments?.getInt("plant_id")!!
            PlantPageScreen(navController,plant_id)
        }

        composable(Screen.Note.route,
            arguments = listOf(navArgument("note_id"){
                type = NavType.IntType
                defaultValue = 0
            })){
            val note_id : Int = it.arguments?.getInt("note_id")!!
            NoteScreen(navController, note_id)
        }

        composable(Screen.Register.route){
            RegistrationScreen(navController)
        }

        composable(Screen.AddPlant.route){
            AddPlantScreen(navController)
        }
    }
}