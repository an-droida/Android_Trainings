package com.android.mvvm_injection_sample.rx_kotlin_sample.models

data class UserProfile (
    var id:Long,
    var name:String,
    var age:Int,
    var image:String
)
//{
//    override fun toString(): String {
//        return "id = $id, " +
//                "name = $name, " +
//                 "age = $age, " +
//                "image = $image"
//    }
//}