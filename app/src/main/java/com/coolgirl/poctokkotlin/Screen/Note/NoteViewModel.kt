package com.coolgirl.poctokkotlin.Screen.Note

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.*
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Common.di.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NoteViewModel : ViewModel() {
    private var noteData : String = ""
    private var isOldImage = false
    var noteText by mutableStateOf("")
    var noteImage : String? = null
    var noteType : Int = 0
    var noteId : Int = 0

    fun UpdateNoteText(text : String){ noteText = text
        SetNote(Notes(GetUser()!!.userid,noteType,noteImage, noteText, noteId,noteData,null))}

    fun IsOldImage(): Boolean{
        return isOldImage
    }

    fun LoadNote(getNoteId : Int){
        if(GetNote()!=null){
            noteText = GetNote()!!.notetext!!
            noteImage = GetNote()!!.image
            noteData = GetNote()!!.notedata!!
            noteId = GetNote()!!.noteid!!
            noteType = GetNote()!!.plantid!!
        }else{
            var notes = GetUser()!!.notes
            if (notes != null) {
                for (note in notes){
                    if(note!!.noteid==getNoteId){
                        noteText = note.notetext!!
                        noteData = note.notedata!!
                        noteId = note.noteid!!
                        noteType = note.plantid!!
                        if(note.image!=null){
                            isOldImage = true
                            noteImage = note.image!!
                        }
                        SetNote(Notes(GetUser()!!.userid,noteType,noteImage, noteText, noteId,noteData,null))
                    }
                }
            }
            SetNote(Notes(GetUser()!!.userid,noteType,noteImage, noteText, noteId,noteData,null))
        }
    }

    fun GetSpinnerData() : List<SpinnerItems>{
        val spinnerList = mutableListOf<SpinnerItems>()
        spinnerList.add(SpinnerItems(0, "Общая"))
        if(GetUser()!=null){
            if(GetUser()!!.plants!=null){
                for(item in GetUser()!!.plants!!){
                    val spinnerItem = item?.plantid?.let { item.plantname?.let { it1 ->
                        SpinnerItems(it, it1) } }
                    spinnerList.add(spinnerItem!!)
                }
            }
        }
        return spinnerList
    }

    fun GetSelectedType(items : List<SpinnerItems>) : Int{
        var i =0;
        for(item in items)
        {
            if(item.Id==noteType){
                return i
            }
            i++
        }
        return 0
    }

    fun GetNoteData() : String{
        if(noteData==""){
            val date = Date()
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = formatter.format(date)
            noteData = formattedDate }
        return noteData
    }

    fun SaveNote(navController: NavController){
        RemoveNote()
        if(noteText==null||noteText==""){ navController.navigate(Screen.UserPage.user_id(GetUser()!!.userid))}
        else{
            noteData = ""
            var note : Notes? = null
            var user = GetUser()
            if (user != null) {
                user.notes = null
                user.plants = null
            }
            note = Notes(GetUser()!!.userid, noteType, noteImage, noteText, noteId, GetNoteData(), user)
            val call1: Call<Notes> = ApiClient().postNote(note)
            call1.enqueue(object : Callback<Notes?> {
                override fun onResponse(call1: Call<Notes?>, response: Response<Notes?>) {
                    if(response.code()==200){
                        RemoveUser()
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