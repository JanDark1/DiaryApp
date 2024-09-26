package com.example.diaryapp.Navigation

 sealed class Screen(val route:String){
     object HomePage:Screen("home_page")
     object AddEditPage:Screen("add_edit_page")
     object LoginPage:Screen("login_page")
     object SignupPage:Screen("signup_page")
 }