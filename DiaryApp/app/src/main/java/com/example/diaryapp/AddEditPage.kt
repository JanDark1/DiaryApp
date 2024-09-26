package com.example.diaryapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel //viewModel için.
import com.example.diaryapp.Data.Diary
import com.example.diaryapp.Navigation.Screen
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPage(navController: NavController,
                viewModel: DiaryViewModel = viewModel(),
                id:Int){ // Buradaki id navigation kısmı tarafından belirlenir



    val diary = remember { mutableStateOf<Diary?>(null) }
    val showCalendarDialog = remember { mutableStateOf(false) }


    LaunchedEffect(id) {
        if (id != 0) {
            viewModel.getADiaryById(id) { fetchedDiary ->
                diary.value = fetchedDiary
                fetchedDiary?.let {
                    viewModel.onDiaryTitleChanged(it.title)
                    viewModel.onDiaryDescriptionChanged(it.description)
                    viewModel.onDiaryDateChanged(it.date)
                }
            }
        }else {
            // Eğer yeni bir günlük oluşturuluyorsa tarih başlangıçta bugünün tarihi olarak ayarlar.
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
            viewModel.onDiaryDateChanged(currentDate)
        }
    }

    if (showCalendarDialog.value) {
        CalendarDialog(
            selectedDate = viewModel.diaryDateState,
            onDateSelected = { date ->
                viewModel.onDiaryDateChanged(date)
                showCalendarDialog.value = false
            },
            onDismissRequest = { showCalendarDialog.value = false }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()) {

                        //Geri
                        IconButton(onClick = { navController.navigate(Screen.HomePage.route) }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                        
                        Spacer(modifier = Modifier.padding(8.dp))
                        
                        Text(text = "Not Yaz")

                        Spacer(modifier = Modifier.weight(1f))

                        //Kaydet
                        IconButton(onClick = {
                            viewModel.save_And_Update_Diary(id)
                            navController.navigate(Screen.HomePage.route) }) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        }


                        // Sil
                        IconButton(onClick = {
                            viewModel.deleteDiaryById(id, onSuccess = {}, onFailure = {})
                            navController.navigate(Screen.HomePage.route)}) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                        }

                    }

                        },

            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Tarih göstergesi
                CalendarText(text = "Tarih: ${viewModel.diaryDateState}",
                             onClick = {showCalendarDialog.value = true})



                //Başlık
                TextField(value = viewModel.diaryTitleState,
                          onValueChange = {viewModel.onDiaryTitleChanged(it)},
                          placeholder = { Text("Başlık Ekleyin") },
                          modifier = Modifier
                              .fillMaxWidth()
                              .clip(RoundedCornerShape(16.dp)),
                          maxLines = 1, // Yazı tek starda tutulur
                          singleLine = true, // ve yana kayar
                          colors = TextFieldDefaults.colors(
                               focusedIndicatorColor = Color.Transparent,// Focus durumunda alt çizgi rengini kaldırır.
                               unfocusedIndicatorColor = Color.Transparent, // Focus dışı durumunda alt çizgi rengini kaldırır.
                               unfocusedPlaceholderColor = Color.Gray,
                               focusedPlaceholderColor = Color.Gray))
                
                Spacer(modifier = Modifier.padding(4.dp))

                //Açıklama
                TextField(value = viewModel.diaryDescriptionState,
                    onValueChange = {viewModel.onDiaryDescriptionChanged(it)},
                    placeholder = { Text("Buraya Yazmaya Başlayın")},
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedPlaceholderColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray
                    )
                    )

            }
        }
    )
}






