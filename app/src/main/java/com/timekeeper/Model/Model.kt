package com.timekeeper.Model

import com.timekeeper.Database.Entity.Activity

data class MyActivity(var name: String, val id: Int) {
    var currentTime: Long = 0
    var comment: String? = null
    var condition: Condition = Condition.INACTIVE
    var timerBase: Long = 0

    fun toDB(): Activity {
        return Activity(id, name, condition.ordinal, timerBase, currentTime, comment)
    }
} //todo нужен полноценный класс, с комментами и временем


enum class Condition {
    ACTIVE,
    INACTIVE
}

object Supplier {
    val activities = listOf<MyActivity>(MyActivity("Sleeping", 0), MyActivity("Coding", 1))
}