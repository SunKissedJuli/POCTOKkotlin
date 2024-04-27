package com.coolgirl.poctokkotlin.Screen.UserPage

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.Common.EncodeImage
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.SetLoginData
import com.coolgirl.poctokkotlin.SetUser
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
import java.io.InputStream

class UserPageViewModel : ViewModel() {
    var user : UserLoginDataResponse? = GetUser()
    var change by mutableStateOf("")
    var DataLoaded by mutableStateOf("")
    private var whatItIs = "plants"

    fun WhatItIs() : String{
        return whatItIs
    }

    fun ShowNotes(){
        whatItIs = "notes"
        change = RandomString()
    }

    fun ShowPlants(){
        whatItIs = "plants"
        change = RandomString()
    }

    fun ShowPhotos(){
        whatItIs = "photos"
        change = RandomString()
    }

    fun GetPhotos(): List<Notes?>?{
        var notes = mutableListOf<Notes>()
        user = GetUser()
        if(user?.notes!=null){
            for(item in user?.notes!!){
                if (item != null && item.image != null && !item.image.equals("")) {
                    notes.add(item)
                }
            }
            return notes.sortedByDescending { it?.noteid }
        }
       return null
    }

    fun GetNotes() : List<Notes?>? {
        var notes = mutableListOf<Notes>()
        user = GetUser()
        if(user?.notes!=null){
            for(item in user?.notes!!){
                if(item==null){
                }else if (item != null && item.image == null || item!!.image.equals("")) {
                    notes.add(item)
                }
            }
            return notes.sortedByDescending { it?.noteid }
        }
        return null

    }

    fun GetPlants() : List<Plant?>? {
        user = GetUser()
        return user?.plants?.sortedByDescending { it?.plantid }
    }

    fun GetPlantName(plantId : Int) : String{
        if(user!=null && user!!.plants!=null){
            for(plant in user!!.plants!!){
                if (plant!!.plantid == plantId && plant.plantname!=null){
                    return plant!!.plantname!!
                }
            }
        }
        return "Общая запись"
    }

    fun LoadNotes(userId : Int){
        var id = userId
        if(id==0){ id = GetUser()!!.userid }

        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<UserLoginDataResponse> = apiClient.getUserProfileData(id)
        call.enqueue(object : Callback<UserLoginDataResponse> {
            override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                if(response.code()==200){
                    user = response.body()
                    user?.let { SetUser(it) }
                    change = RandomString()
                    DataLoaded = RandomString()
                }
            }
            override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                //дописать
            } })
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
                        PutUserImage(EncodeImage(file!!.path))
                    }
                }
            }finally { cursor!!.close() }
        }
        return launcher
    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    fun PutUserImage(userIcon : String){
        var User = GetUser()
        User?.userimage = userIcon
        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<UserLoginDataResponse> = apiClient.createUser(User)
        call.enqueue(object : Callback<UserLoginDataResponse> {
            override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                if(response.code()==200){
                    response.body()?.let { SetUser(it) }
                    DataLoaded = RandomString()
                }
            }
            override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                //дописать
            } })
    }

}