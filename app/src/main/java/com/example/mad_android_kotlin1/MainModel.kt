package com.example.mad_android_kotlin1

data class MainModel(
    var name: String = "",
    var price: Int = 0,
    var phone: Int = 0,
    var turl: String = ""
) {

    constructor() : this("", 0, 0, "")
}
