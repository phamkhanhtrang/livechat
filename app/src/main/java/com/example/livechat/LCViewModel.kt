package com.example.livechat

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.ConsoleMessage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.livechat.data.CHATS
import com.example.livechat.data.ChatData
import com.example.livechat.data.ChatUser
import com.example.livechat.data.Event
import com.example.livechat.data.MESSAGE
import com.example.livechat.data.Message
import com.example.livechat.data.STATUS
import com.example.livechat.data.Status
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.UseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.annotation.CheckForSigned
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel()
{

    var inProcess = mutableStateOf(false)
    var inProcessChats = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UseData?>(null)
    val chats= mutableStateOf<List<ChatData>>( listOf())

    val chatMessage= mutableStateOf<List<Message>>(listOf())
    val inProcessChatMessage= mutableStateOf(false)
    var currentChatMessageListen: ListenerRegistration?=null

    val status = mutableStateOf<List< Status>>(listOf())
    val inProcessStatus = mutableStateOf(false)

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }
    fun populateMessages(chatId: String){
        inProcessChatMessage.value=true
        currentChatMessageListen = db.collection(CHATS).document(chatId).collection(MESSAGE).addSnapshotListener{value, error->
            if(error!=null){
                handleException(error)

            }
            if (value!=null){
                chatMessage.value=value.documents.mapNotNull {
                    it.toObject<Message>()
                }.sortedBy {
                    it.timestamp
                }
                inProcessChatMessage.value=false
            }
        }
    }
    fun depopulateMessage(){
        chatMessage.value= listOf()
        currentChatMessageListen= null
    }

    fun populateChats(){
        inProcessChats.value=true
        db.collection(CHATS).where(
            Filter.or(
            Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener{
            value, error ->
            if(error!=null){
                handleException(error)
            }
            if (value!=null){
                chats.value= value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProcessChats.value= false
            }
        }
    }

    fun onSendReply(chatID: String, message: String){
        val time = Calendar.getInstance().time.toString()
        val msg= Message(userData.value?.userId,message, time)
        db.collection(CHATS).document(chatID).collection(MESSAGE).document().set(msg)

    }

    fun signUp(name: String, number: String, email: String, password: String) {
        inProcess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = " Please Fill")
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number,)
                        Log.d("TAG", "signup: User Logged In")
                    }
                    else {
                        handleException(it.exception, customMessage = "sign up failed")
                    }
                }
            } else {
                handleException(customMessage = "number already exit")
                inProcess.value = false
            }
        }
    }
    fun loginIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill")
            return

        } else {
            inProcess.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        inProcess.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    } else {
                        handleException(exception = it.exception, customMessage = " login failed")
                    }
                }
        }
    }
    fun upLoadProfileImage(uri: Uri){
        upLoadImage(uri){
        createOrUpdateProfile(imageUrl = it.toString())
        }
    }
    fun upLoadImage(uri: Uri, onSuccess:(Uri)->Unit){
        inProcess.value= true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("image/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProcess.value= false
        }
            .addOnFailureListener{
                handleException(it)
            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        var uid = auth.currentUser?.uid
        val useData = UseData(
            userId = uid,
            name = name?: userData.value?.name,
            number = number?: userData.value?.number,
            imageUrl = imageUrl?: userData.value?.imageUrl

        )
        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    // update user data
                } else {
                    db.collection(USER_NODE).document(uid).set(useData)
                    inProcess.value = false
                    getUserData(uid)
                }

            }.addOnFailureListener() {
                handleException(it, "Cannot Retriev User")
            }

        }
    }

    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, " Con not retreive User")
            }
            if (value != null) {
                var user = value.toObject<UseData>()
                userData.value = user
                inProcess.value = false
                populateChats()
                populateStatuses()
            }
        }
    }


    fun handleException(exception: Exception? = null, customMessage: String="") {
        Log.e("Live chat app ", "Live chat execption:", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNotEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = false
    }
    fun logout(){
        auth.signOut()
        signIn.value=false
        userData.value=null
//        depopulateMessage()
        eventMutableState.value=Event("Logged Out")
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or ! number.isDigitsOnly()){
            handleException(customMessage = "Number must be cintain digitas")
        }else{
            db.collection(CHATS).where(Filter.or(
                Filter.and(
                    Filter.equalTo("user1.number", number),
                    Filter.equalTo("user2.number", userData.value?.number)
            ),
                Filter.and(
                    Filter.equalTo("user1.number", userData.value?.number),
                    Filter.equalTo("user2.number",number )
                ),
            )).get().addOnSuccessListener {
                if (it.isEmpty){
                    db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
                        if(it.isEmpty){
                            handleException(customMessage = "number not found")
                        }else{
                            val chatPartner =it.toObjects<UseData>()[0]
                            val id = db.collection(CHATS).document().id
                            val chat = ChatData(
                                chatID = id,
                                ChatUser(userData.value?.userId,
                                    userData.value?.name,
                                    userData.value?.imageUrl,
                                    userData.value?.number),
                                ChatUser(chatPartner.userId,
                                    chatPartner.name, chatPartner.imageUrl, chatPartner.number)
                            )
                            db.collection(CHATS).document(id).set(chat)
                        }
                    }
                        .addOnFailureListener {

                                    handleException(it)

                        }
                }else{
                    handleException(customMessage = "chat already exists")
                }
            }
        }
    }

    fun uploadStatus(uri: Uri) {
        upLoadImage(uri){
            createStatus(it.toString())

        }
    }
    fun createStatus(imageurl: String){
        val newStatus = Status(
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number,
            ),
            imageurl,
            System.currentTimeMillis()
        )
        db.collection(STATUS).document().set(newStatus)
    }

    fun populateStatuses(){
        val timeDelta = 24L *60 *60 *1000
        val cutOff = System.currentTimeMillis()- timeDelta
        inProcessStatus.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener{
            value, error->
            if(error!=null)
                handleException(error)
            if (value!=null){
                val currentConnections = arrayListOf(userData.value?.userId)
                val chats = value.toObjects<ChatData>()
                chats.forEach{
                    chat ->
                    if(chat.user1.userId == userData.value?.userId){
                        currentConnections.add(chat.user2.userId)
                    }else
                        currentConnections.add(chat.user1.userId)
                }
                db.collection(STATUS).whereGreaterThan("timestamp", cutOff).whereIn("user.userId",
                    currentConnections).addSnapshotListener{  value, error ->
                        if(error!=null){
                            handleException(error)
                        }
                    if (value!=null){
                        status.value= value.toObjects()
                        inProcessStatus.value= false
                    }
                }
            }
        }
    }

}

