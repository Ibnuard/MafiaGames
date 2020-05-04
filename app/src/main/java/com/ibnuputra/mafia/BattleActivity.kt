package com.ibnuputra.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ibnuputra.mafia.Adapter.ChatAdapter
import com.ibnuputra.mafia.Adapter.UserListAdapter
import com.ibnuputra.mafia.Model.ChatModel
import com.ibnuputra.mafia.Model.PlayerModel
import com.ibnuputra.mafia.Model.UserModel
import kotlinx.android.synthetic.main.activity_battle.*

class BattleActivity : AppCompatActivity() {
    private lateinit var chAdapter: ChatAdapter
    private lateinit var usrAdapter: UserListAdapter
    private lateinit var resultList:MutableList<ChatModel>
    private lateinit var resultUser: MutableList<PlayerModel>
    private lateinit var chRecycler: RecyclerView
    private lateinit var usrRecycler: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mRef: DatabaseReference
    private lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle)

        chRecycler = findViewById(R.id.recycler_chat)
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        mUser = mAuth.currentUser!!
        val rid = intent.getStringExtra("RID")

        chRecycler.layoutManager = LinearLayoutManager(this)
        resultList = ArrayList()

        btnSend.setOnClickListener {
            val usrMsg = chatForm?.text.toString()
            if (usrMsg.isNotEmpty()){
                getUserData(usrMsg, rid)
            }
        }

        getChatData(rid)
    }

    private fun getChatData(rid: String?) {
        mRef.child("Rooms").child(rid!!).child("Chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    resultList.clear()
                    for (npsnapshot in p0.children) {
                        val l: ChatModel? = npsnapshot.getValue(ChatModel::class.java)
                        resultList.add(l!!)
                    }
                    chRecycler.setHasFixedSize(true)
                    resultList.reverse()
                    chAdapter = ChatAdapter(resultList, this@BattleActivity)
                    chRecycler.adapter = chAdapter
                }
            })
    }

    private fun getUserData(msg: String, rid: String) {
        val uid = mUser.uid
        mRef.child("Users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val userData = p0.getValue(UserModel::class.java)
                val chatMsg = ChatModel(userData?.name!!, msg, userData?.avatar)
                mRef.child("Rooms").child(rid).child("Chat").push().setValue(chatMsg).addOnCompleteListener {
                    chatForm.text = null
                }
            }

        })
    }
}
