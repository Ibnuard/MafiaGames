package com.ibnuputra.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ibnuputra.mafia.Model.StatsModel
import com.ibnuputra.mafia.Model.UserModel
import kotlinx.android.synthetic.main.activity_room.*
import kotlin.random.Random

class RoomActivity : AppCompatActivity() {
    private lateinit var mref: DatabaseReference
    private lateinit var mauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        mauth = FirebaseAuth.getInstance()
        mref = FirebaseDatabase.getInstance().reference

        btnCreateRoom.setOnClickListener {
            //create random room
            val rid = Random.nextInt(111111, 999999).toString()
            val stats = StatsModel(0, 0, 0, 0)
            mref.child("Rooms").child(rid).setValue(stats).addOnCompleteListener {
                if (it.isSuccessful){
                    getUserProfile(rid)
                }
            }
        }

        btnOkRoom.setOnClickListener {
            val rid = ed_room.text.toString()
            if (rid.isNotEmpty()){
                mref.child("Rooms").child(rid).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            getUserProfilenoAdmin(rid)
                        }
                    }

                })
            }
        }
    }

    private fun getUserProfilenoAdmin(rid: String) {
        val uid = mauth.currentUser?.uid
        mref.child("Users").child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val player: UserModel? = p0.getValue(UserModel::class.java)
                mref.child("Rooms").child(rid).child("Users").child(uid).setValue(player).addOnCompleteListener {
                    if (it.isSuccessful){
                        if (it.isSuccessful){
                            val intent = Intent(this@RoomActivity, WaitingActivity::class.java)
                            intent.putExtra("RID", rid)
                            startActivity(intent)
                            finish()
                        }
                    }
                }

            }

        })

    }

    private fun getUserProfile(rid: String?) {
        val uid = mauth.currentUser?.uid
        mref.child("Users").child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val player: UserModel? = p0.getValue(UserModel::class.java)
                mref.child("Rooms").child(rid!!).child("Users").child(uid).setValue(player)
                mref.child("Rooms").child(rid).child("Users").child(uid).child("admin").setValue(1).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this@RoomActivity, WaitingActivity::class.java)
                        intent.putExtra("RID", rid)
                        startActivity(intent)
                        finish()
                    }
                }

            }

        })
    }
}
