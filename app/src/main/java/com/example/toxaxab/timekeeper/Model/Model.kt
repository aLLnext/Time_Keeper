package com.example.toxaxab.timekeeper.Model

data class MyActivity(var name: String) //todo нужен полноценный класс, с комментами и временем

object Supplier{
    val activities = listOf<MyActivity>(MyActivity("Sleeping"), MyActivity("Coding"))
}