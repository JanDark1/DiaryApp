package com.example.diaryapp.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.diaryapp.AddEditPage
import com.example.diaryapp.Authentication.AuthViewModel
import com.example.diaryapp.Authentication.LoginPage
import com.example.diaryapp.Authentication.SignupPage
import com.example.diaryapp.DiaryViewModel
import com.example.diaryapp.HomePage

@Composable
fun Navigation(navController: NavHostController = rememberNavController(),
               modifier: Modifier = Modifier,
               viewModel: DiaryViewModel = viewModel(),
               autViewModel: AuthViewModel = viewModel()){
    NavHost(navController = navController,
            startDestination = Screen.LoginPage.route){

        composable(Screen.HomePage.route){
            HomePage(navController,modifier,viewModel,autViewModel)
        }

        composable(Screen.AddEditPage.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ){entry ->
            val id = if (entry.arguments != null) entry.arguments!!.getInt("id") else 0
            AddEditPage( navController = navController , id = id )
        }

        composable(Screen.LoginPage.route){
            LoginPage(navController = navController, authViewModel = autViewModel )
        }

        composable(Screen.SignupPage.route){
            SignupPage(navController = navController, authViewModel = autViewModel )
        }


    }
}