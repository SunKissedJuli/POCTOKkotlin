package com.coolgirl.poctokkotlin.Screen.ImageChoice

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.coolgirl.poctokkotlin.Common.EncodeImage
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.Common.getResourceNameFromDrawableString
import com.coolgirl.poctokkotlin.GetNote
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.SetNote
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

class ImageChoiceViewModel : ViewModel() {
    var image : String? = null
    var newImage : File? = null
    var whatItIs : String? = null
    var go by mutableStateOf("")

    @SuppressLint("Range", "SuspiciousIndentation")
    @Composable
    fun OpenGalery(navController: NavHostController,context: Context = LocalContext.current): ManagedActivityResultLauncher<String, Uri?> {
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
                        image = EncodeImage(file!!.path)

                        when(whatItIs){
                            "note" ->{ var note = GetNote()
                                if(image!=null){
                                    note!!.image = image
                                    SetNote(note) }
                                navController.navigate(Screen.Note.note_id(note!!.noteid))
                            }
                            "user_page" ->{
                                PutUserImage(image!!)
                                if(go!=null&&!go.equals("")){
                                    navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))
                                }
                            }
                        }
                    }
                }

            }finally { cursor!!.close() }
        }
        if (file != null && file != newImage) {
            newImage = file
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
                    go = "1"
                }
            }
            override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                //дописать
            } })
    }

    @Composable
    fun GetFileFromDrawable(image: Int, navController : NavHostController) {
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
                var image = EncodeImage(file!!.path)
                when(whatItIs){
                    "note" ->{ var note = GetNote()
                        if(image!=null){
                            note!!.image = image
                            SetNote(note) }
                        navController.navigate(Screen.Note.note_id(note!!.noteid))
                    }
                    "user_page" ->{
                        PutUserImage(image!!)
                        if(go!=null&&!go.equals("")){
                            navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))
                        }
                    }
                }
            }
        }
    }

}