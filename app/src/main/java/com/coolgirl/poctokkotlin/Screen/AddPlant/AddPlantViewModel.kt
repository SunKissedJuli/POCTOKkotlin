package com.coolgirl.poctokkotlin.Screen.AddPlant

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.*
import com.coolgirl.poctokkotlin.commons.EncodeImage
import com.coolgirl.poctokkotlin.commons.copyStreamToFile
import com.coolgirl.poctokkotlin.data.dto.Plant
import com.coolgirl.poctokkotlin.data.dto.WateringSchedule
import com.coolgirl.poctokkotlin.di.ApiClient
import com.coolgirl.poctokkotlin.commons.getResourceNameFromDrawableString
import com.coolgirl.poctokkotlin.data.dto.PlantIdentification
import com.coolgirl.poctokkotlin.data.dto.PlantIdentificationResponse
import com.coolgirl.poctokkotlin.di.PlantIdApiClient
import com.coolgirl.poctokkotlin.navigate.Screen
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddPlantViewModel : ViewModel() {
    var plantNickname by mutableStateOf("")
    var plantDescription by mutableStateOf("")
    var plant : Plant? = null
    var plantImage by mutableStateOf("")
    val fileName = mutableStateOf(0)
    @OptIn(ExperimentalMaterialApi::class)
    var sheetState: ModalBottomSheetState? = null
    var scope: CoroutineScope? = null

    fun UpdatePlantNickname(nickname: String) {
        plantNickname = nickname
        plant!!.plantname = plantNickname
        SetPlant(plant!!)
    }
    fun UpdatePlantDescription(description : String){
        plantDescription = description
        plant!!.plantdescription = plantDescription
        SetPlant(plant!!)}

    fun CreatePlant(){ plant = GetPlant() }

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
            val call: Call<Plant> = ApiClient().postPlant(plant)
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

    @SuppressLint("Range", "SuspiciousIndentation")
    @Composable
    fun OpenGalery(context: Context = LocalContext.current): ManagedActivityResultLauncher<String, Uri?> {
        var file by remember { mutableStateOf<File?>(null) }
        val coroutineScope = rememberCoroutineScope()
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cursor: Cursor? = context.getContentResolver().query(uri!!, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    var fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    val iStream : InputStream = context.contentResolver.openInputStream(uri!!)!!
                    val outputDir : File = context.cacheDir
                    val outputFile : File = File(outputDir,fileName)
                    copyStreamToFile(iStream, outputFile)
                    iStream.close()
                    coroutineScope.launch() {
                        file = Compressor.compress(context, outputFile!!) {
                            default(width = 50, format = Bitmap.CompressFormat.JPEG) }
                        IdentificatePlant(EncodeImage(file!!.path))
                    }
                }

            }finally { cursor!!.close() }
        }
        return launcher
    }

    fun IdentificatePlant(encodeImage: String) {
        var list: MutableList<String>? = mutableListOf()
        list?.add("data:image/jpg;base64," + encodeImage)
        var plant = list?.let { PlantIdentification(it) }
        val call: Call<PlantIdentificationResponse> = PlantIdApiClient().IdentityPlantForPhoto(plant!!)
        call.enqueue(object : Callback<PlantIdentificationResponse> {
            override fun onResponse(call: Call<PlantIdentificationResponse>, response: Response<PlantIdentificationResponse>) {
                if(response.code()==200||response.code()==201) {
                    if(response.body()!!.result.is_plant.binary){
                        var suggestions = response.body()!!.result.classification.suggestions.get(0)
                        plantNickname = suggestions.details.common_names?.get(0) ?: plantNickname
                    }else{
                        //вывод сообщения о том, что это не растение
                    }
                    plantImage = encodeImage
                }

            }
            override fun onFailure(call: Call<PlantIdentificationResponse>, t: Throwable) {
                Log.d("tag","хуй response " + t.message)
            } })
    }
}
