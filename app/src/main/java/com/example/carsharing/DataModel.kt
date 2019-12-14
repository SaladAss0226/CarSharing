package com.example.carsharing

data class RequestSignup(
    val email: String,
    val password: String,
    val password_confirmation: String
)

data class ResponseSignup(
    val data: List<Any>,
    val message: String,
    val result: Boolean
)

data class RequestLogin(
    val email: String,
    val password: String
)

data class ResponseLogin(
    val data: List<Token>,
    val message: String,
    val result: Boolean
)

data class Token(
    val token: String
)