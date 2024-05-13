package com.example.livechat.data

import java.sql.Timestamp

data class UseData (
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?="",
){
    fun toMap()= mapOf(
        "userID" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )
}
data class ChatData(
    val chatID: String?="",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser=ChatUser(),

)
data class  ChatUser(
    val userId: String?="",
    val name: String?="",
    val imageUrl: String?="",
    val number: String?="",
)
data class Message(
    var sendBy:String?="",
    val message: String?="",
    val timestamp: String?=""
)
data class Status(val user: ChatUser = ChatUser(),
    val imageUrl: String?= "",
    val timestamp: Long?=null
)