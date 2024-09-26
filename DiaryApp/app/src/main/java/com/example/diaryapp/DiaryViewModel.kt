package com.example.diaryapp


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.diaryapp.Data.Diary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class DiaryViewModel():ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = Firebase.database.reference
    private val countRef: DatabaseReference = db.child("system").child("count")
    private val _diaryList = MutableStateFlow<List<Diary>>(emptyList())
    val diaryList = _diaryList.asStateFlow()
    val emailKey = getUserEmailKey()
    var diaryTitleState by mutableStateOf("")
    var diaryDescriptionState by mutableStateOf("")
    var diaryDateState by mutableStateOf("")






    init {
        auth.addAuthStateListener {
            val user = it.currentUser
            if (user != null) {
                getDiaryList()
            } else {
                _diaryList.value = emptyList()
            }
        }
    }

    private fun getUserEmailKey(): String {
        return auth.currentUser?.email?.replace(".", ",") ?: ""
    }





    fun getDiaryList() {
        val emailKey = getUserEmailKey()
        db.child("users").child(emailKey).child("diaries").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val diaries = mutableListOf<Diary>()
                for (diarySnapshot in snapshot.children) {
                    val diary = diarySnapshot.getValue(Diary::class.java)
                    diary?.let { diaries.add(it) }
                }
                _diaryList.value = diaries
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Veri okunurken hata oluştu: ${error.message}")
            }
        })
    }



     fun saveDiary() {
        countRef.get().addOnSuccessListener { snapshot ->
            val currentCount = snapshot.getValue(Int::class.java) ?: 0
            val newDiary = Diary(
                id = currentCount,
                title = diaryTitleState,
                description = diaryDescriptionState,
                date = diaryDateState // Tarih burada ekleniyor
            )
            db.child("users").child(emailKey).child("diaries").child(currentCount.toString())
                .setValue(newDiary)
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Günlük eklenirken hata oluştu: ${e.message}")
                }
            countRef.setValue(currentCount + 1)
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Sayaç değeri alınırken hata oluştu: ${e.message}")
        }
    }



    fun save_And_Update_Diary(id: Int?) {
        if (id != null && id != 0) {
            val updatedDiary = Diary(
                id = id,
                title = diaryTitleState,
                description = diaryDescriptionState,
                date = diaryDateState // Güncelleme yapılıyorsa mevcut tarih korunur
            )
            db.child("users").child(emailKey).child("diaries").child(id.toString()).setValue(updatedDiary)
                .addOnSuccessListener {
                    Log.d("Firebase", "Günlük başarıyla güncellendi.")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Günlük güncellenirken hata oluştu: ${e.message}")
                }
        } else {
            saveDiary()
        }
    }

    fun deleteDiaryById(diaryId: Int, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.child("users").child(emailKey).child("diaries").child(diaryId.toString()).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun getADiaryById(diaryId: Int, callback: (Diary?) -> Unit) {
        db.child("users").child(emailKey).child("diaries").child(diaryId.toString()).get().addOnSuccessListener { snapshot ->
            val diary = snapshot.getValue(Diary::class.java)
            callback(diary)
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Günlük alınırken hata oluştu: ${e.message}")
            callback(null)
        }
    }



    fun onDiaryTitleChanged(newString:String){
        diaryTitleState = newString
    }

    fun onDiaryDescriptionChanged(newString:String){
        diaryDescriptionState = newString
    }

    fun onDiaryDateChanged(newString: String){
        diaryDateState = newString
    }
}

