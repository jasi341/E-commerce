package com.jasmeet.e_commerce.model

data class UserInfo (
    val userName : String,
    val userEmail : String,
    val imagePath : String = ""
){
    constructor() : this("","","")
}