package com.example.diaryapp.Authentication

sealed class AuthState {//Gerçekleşen durumlaru kullanıcı arayüzüne yansıtmamızı sağlar.
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}