package com.example.diaryapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.diaryapp.Navigation.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diaryapp.Authentication.AuthViewModel
import com.example.diaryapp.Data.Diary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DiaryViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val diaryList by viewModel.diaryList.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Günlük ") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.LoginPage.route)
                            authViewModel.signout()
                        }
                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Exit Account")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = Color.Black,
                onClick = { navController.navigate(Screen.AddEditPage.route + "/0") }
            ) {
                Text(text = "+", fontSize = 25.sp)
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(diaryList) { diary ->
                    DiaryItem(
                        diary = diary,
                        onClick = { navController.navigate(Screen.AddEditPage.route + "/${diary.id}") }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryItem(diary: Diary, onClick: () -> Unit, viewModel: DiaryViewModel = viewModel()) {
    val expanded = remember { mutableStateOf(false) }
    val isCardExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = {
                    onClick()
                    isCardExpanded.value = !isCardExpanded.value
                },
                onLongClick = { expanded.value = true }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Sol Tarafdaki Tarih.
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)
                    .background(Color.Transparent)
            ) {
                CalendarDesign(date = diary.date)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Sağ Taraftaki içerik.
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (isCardExpanded.value) diary.title else diary.title.take(50) +
                            if (diary.title.length > 50) "..." else "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isCardExpanded.value) diary.description else diary.description.take(30) +
                            if (diary.description.length > 30) "..." else "",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text("Sil") },
                onClick = {
                    viewModel.deleteDiaryById(diary.id,
                        onSuccess = { expanded.value = false },
                        onFailure = { exception -> expanded.value = false })
                }
            )
        }
    }
}





























