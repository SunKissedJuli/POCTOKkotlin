package com.coolgirl.poctokkotlin.Screen.Note

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.Common.DecodeImage
import com.coolgirl.poctokkotlin.Common.EncodeImage
import com.coolgirl.poctokkotlin.Common.EncodeImageToServer
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.Notes
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
import java.text.SimpleDateFormat
import java.util.*

class NoteViewModel : ViewModel() {
    var noteText by mutableStateOf("")
    var noteImage by mutableStateOf("")
    var newImage : File? = null
    private var noteData : String = ""
    var noteId : Int = 0

    fun LoadNote(getNoteId : Int){
        var notes = GetUser()!!.notes
        if (notes != null) {
            for (note in notes){
                if(note!!.noteid==getNoteId){
                    noteText = note.notetext!!
                    noteData = note.notedata!!
                    noteId = note.noteid!!
                    if(note.image!=null){
                        noteImage = note.image!!
                    }
                }
            }
        }
    }

    fun GetNoteData() : String{
        if(noteData==""){
            val date = Date()
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = formatter.format(date)
            noteData = formattedDate }
        return noteData
    }

    fun UpdateNoteText(text : String){ noteText = text }

    @SuppressLint("Range", "SuspiciousIndentation")
    @Composable
    fun OpenGalery(context: Context = LocalContext.current): ManagedActivityResultLauncher<String, Uri?>{
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
                                    default(width = 50, format = Bitmap.CompressFormat.JPEG)

                                }
                                noteImage = EncodeImage(file!!.path)
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

    fun SaveNote(navController: NavController){
        if(noteText==null||noteText==""){ navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))}

        else{
            noteData = ""
            var note : Notes? = null
            var user = GetUser()
            if (user != null) {
                user.notes = null;
                user.plants = null;
            }

            if(noteImage!=null){
                note = Notes(GetUser()!!.userid, 0, noteImage, noteText, noteId, GetNoteData(), user)
            }else{
                note = Notes(GetUser()!!.userid, 0, noteImage, noteText, noteId, GetNoteData(), user)
            }
            var apiClient = ApiClient.start().create(ApiController::class.java)
            val call1: Call<Notes> = apiClient.postNote(note)
            call1.enqueue(object : Callback<Notes?> {
                override fun onResponse(call1: Call<Notes?>, response: Response<Notes?>) {
                    Log.d("tag", "В Note (SaveNote) ответ от сервера: " + response.code())
                    if(response.code()==200){
                        response.body()?.user?.let { SetUser(it) }
                        navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))
                    }
                }
                override fun onFailure(call1: Call<Notes?>, t: Throwable) {
                    Log.d("tag", "В Note (SaveNote) нет ответа от сервера " + t.message)
                }
            })
        }
    }
}