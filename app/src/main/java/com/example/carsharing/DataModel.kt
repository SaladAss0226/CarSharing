package com.example.carsharing

data class ResponseLogout(
    val data: List<Any>,
    val message: String,
    val result: Boolean
)

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

data class RequestPost(
    val departure: String,
    val departure_date: String,
    val description: String,
    val destination: String,
    val seat: String,
    val subject: String
)

data class ResponsePost(
    val data: List<PostDetail>,
    val message: String,
    val result: Boolean
)

data class PostDetail(
    val created_at: String,
    val departure: String,
    val departure_date: String,
    val description: String,
    val destination: String,
    val id: Int,
    val seat: String,
    val subject: String,
    val updated_at: String,
    val user_id: Int
)

data class ResponseAllposts(
    val data: List<AllpostsDetails>,
    val links: Links,
    val meta: Meta
)

data class AllpostsDetails(
    val created_at: String,
    val departure: String,
    val departure_date: String,
    val description: String,
    val destination: String,
    val id: Int,
    val ptt_url: Any,
    val seat: Int,
    val subject: String,
    val type: Int,
    val user_id: Int
)

data class Links(
    val first: String,
    val last: String,
    val next: Any,
    val prev: Any
)

data class Meta(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val path: String,
    val per_page: Int,
    val to: Int,
    val total: Int
)



//搜尋
data class ResponseSearch(
    val data: List<SearchDetails>,
    val links: Links,
    val meta: Meta
)

data class SearchDetails(
    val created_at: String,
    val departure: String,
    val departure_date: String,
    val description: String,
    val destination: String,
    val id: Int,
    val ptt_url: Any,
    val seat: Int,
    val subject: String,
    val type: Int,
    val user_id: Int
)
