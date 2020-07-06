package com.example.loginchatudemy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginchatudemy.R
import com.example.loginchatudemy.adapter.ChatAdapter
import com.example.loginchatudemy.models.Message
import com.example.loginchatudemy.models.TotalMessagesEvent
import com.example.loginchatudemy.toast
import com.example.loginchatudemy.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.fragment_chat.view.editTextMessage
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {

    //INICIO VARIABLES
    private lateinit var _view: View
    private lateinit var adapter: ChatAdapter
    private val messageList: ArrayList<Message> = ArrayList()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser
    private val store: FirebaseFirestore = FirebaseFirestore.getInstance() //Obtenemos la instancia del servicio
    private lateinit var chatDBRef: CollectionReference
    private var chatSubscription: ListenerRegistration? = null
    //FIN VARIABLES

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _view = inflater.inflate(R.layout.fragment_chat, container, false)

        //INICIO LLAMADA DE METODOS
        setUpDB()
        sepUpCurrentUser()
        setUpRecyclerView()
        setUpChatBtn()
        subscribeToChatMessage()
        //FIN LLAMADA DE METODOS
        return _view
    }

    //INICIO METODOS/FUNCIONES
    private fun setUpDB(){
        chatDBRef = store.collection("chat")
    }
    private fun sepUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }
    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(messageList, currentUser.uid)
        _view.recyclerView.setHasFixedSize(true)
        _view.recyclerView.layoutManager = layoutManager
        _view.recyclerView.itemAnimator = DefaultItemAnimator()
        _view.recyclerView.adapter = adapter
    }
    private fun setUpChatBtn(){
        _view.buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val photo = currentUser.photoUrl?.let { currentUser.photoUrl.toString() } ?: run {""}
                val message = Message(currentUser.uid, messageText, photo, Date())
                saveMessage(message)
                _view.editTextMessage.setText("")
            }
        }
    }
    private fun saveMessage(message: Message){
        val newMessage = HashMap<String, Any>()
        newMessage["authorId"] = message.authorId
        newMessage["message"] = message.message
        newMessage["profileImageUrl"] = message.profileImageUrl
        newMessage["sendAt"] = message.sendAt
        chatDBRef.add(newMessage).addOnCompleteListener {
            activity!!.toast("Mensaje enviado")
        }
            .addOnFailureListener {
                activity!!.toast("Error al mandar el mensaje")
            }
    }
    private fun subscribeToChatMessage(){
        chatSubscription = chatDBRef
            .orderBy("sendAt", Query.Direction.DESCENDING)
            .limit(100)
            .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
            override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                exception?.let {
                    activity!!.toast("Exception!")
                    return
                }

                snapshot?.let {
                    messageList.clear()
                    val messages = it.toObjects(Message::class.java)
                    messageList.addAll(messages.asReversed())
                    adapter.notifyDataSetChanged()
                    _view.recyclerView.smoothScrollToPosition(messageList.size)
                    RxBus.publish(TotalMessagesEvent(messageList.size))

                }
            }
        })
    }
    override fun onDestroyView() {
        chatSubscription?.remove()
        super.onDestroyView()
    }
    //FIN METODOS/FUNCIONES
}
