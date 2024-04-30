package com.coolgirl.poctokkotlin.Screen.AddPlant

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.*
import com.coolgirl.poctokkotlin.Common.EncodeImage
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.WateringSchedule
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class AddPlantViewModel : ViewModel() {
    var plantNickname by mutableStateOf("")
    var plantDescription by mutableStateOf("")
    var plant : Plant? = null
    var plantImage by mutableStateOf("")

    fun UpdatePlantNickname(nickname: String) {
        plantNickname = nickname
        plant!!.plantname = plantNickname
        SetPlant(plant!!)
    }
    fun UpdatePlantDescription(description : String){
        plantDescription = description
        plant!!.plantdescription = plantDescription
        SetPlant(plant!!)}

    fun CreatePlant(){
      //  SetPlant(Plant(null,null,0,null, GetUser()!!.userid, GetUserFor(),null,null))
        plant = GetPlant()
    }

    fun Save(navController: NavController){
        var plant = GetPlant()
        if(plantImage!=null){
            plant!!.plantimage = plantImage
        }
        if(plant!!.wateringSchedule==null){
           plant.wateringSchedule = WateringSchedule(plant.plantid, plant.userid, "0000000", 0, GetPlantFor() )
        }
        if(plantNickname==null){
            //дописать
        }else{
            var apiClient = ApiClient.start().create(ApiController::class.java)
            val call: Call<Plant> = apiClient.postPlant(plant)
            call.enqueue(object : Callback<Plant> {
                override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                    if(response.code()==200){
                        response.body()?.let { SetPlant(it) }
                        navController.navigate(Screen.PlantPage.plant_id(response.body()!!.plantid!!))
                    }else if(response.code()==204){
                        navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))
                    }
                }
                override fun onFailure(call: Call<Plant>, t: Throwable) {
                } })
        }
    }

    @Composable
    fun GetFileFromDrawable(image: Int) {
        if (image != 0) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            var file by remember { mutableStateOf<File?>(null) }
            val bitmap = BitmapFactory.decodeResource(context.resources, image)

            coroutineScope.launch {
                val extStorageDirectory = context.cacheDir
                val name = getResourceNameFromDrawableString(image.toString()) + ".JPEG"
                val outputFile = File(extStorageDirectory, name)
                FileOutputStream(outputFile).use { outStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                }
                file = Compressor.compress(context, outputFile) {
                    default(width = 50, format = Bitmap.CompressFormat.JPEG)
                }
                plantImage = EncodeImage(file!!.path)
            }
        }
    }

    fun getResourceNameFromDrawableString(drawableString: String): String {
        val parts = drawableString.split(".")
        return if (parts.size == 3 && parts[0] == "R" && parts[1] == "drawable") {
            parts[2]
        } else {
            ""
        }
    }
}
