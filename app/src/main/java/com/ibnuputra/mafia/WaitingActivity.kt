package com.ibnuputra.mafia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ibnuputra.mafia.Adapter.UserListAdapter
import com.ibnuputra.mafia.Model.UserModel
import kotlinx.android.synthetic.main.activity_waiting.*
import kotlin.random.Random

class WaitingActivity : AppCompatActivity() {
    private lateinit var adapter: UserListAdapter
    private lateinit var resultList:MutableList<UserModel>
    private lateinit var recycler: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mRef: DatabaseReference
    private lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val rid = intent.getStringExtra("RID")
        rid_tv.text = rid

        recycler = findViewById(R.id.recycler_view)
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        mUser = mAuth.currentUser!!

        recycler.layoutManager = GridLayoutManager(this, 3)
        resultList = ArrayList()

        getUserList(rid)

        btnPlay.setOnClickListener {
            mRef.child("Rooms").child(rid).child("Users").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    val role = intArrayOf(0,1)
                    var mafia = 0
                    for (npsnapshot in p0.children){
                        Log.d("TAG", "MAFIA $mafia")
                        val numb = role[Random.nextInt(0, 2)]
                        Log.d("TAG", "NUMB $numb")
                        when(numb){
                            1 -> mafia+=1
                        }
                        when(mafia){
                            1 -> npsnapshot.child("role").ref.setValue(0)
                            else -> npsnapshot.child("role").ref.setValue(numb)
                        }
                    }
                }

            })
        }
    }

    private fun getUserList(rid: String) {
        mRef.child("Rooms").child(rid).child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(p0: DataSnapshot) {
                    resultList.clear()
                    for (npsnapshot in p0.children) {
                        val l: UserModel? = npsnapshot.getValue(UserModel::class.java)
                        resultList.add(l!!)
                    }
                    recycler.setHasFixedSize(true)
                    resultList.reverse()
                    adapter = UserListAdapter(resultList, this@WaitingActivity)
                    recycler.adapter = adapter
                }
            })
    }
}
