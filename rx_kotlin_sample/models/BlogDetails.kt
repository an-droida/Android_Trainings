package com.android.mvvm_injection_sample.rx_kotlin_sample.models

data class BlogDetails(
    var id: Long,
    var userId: Long,
    var title: String,
    var content: String,
    val user: User
)