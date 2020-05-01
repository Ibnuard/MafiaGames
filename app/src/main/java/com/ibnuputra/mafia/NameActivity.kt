package com.ibnuputra.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_name.*

class NameActivity : AppCompatActivity() {
    private lateinit var mref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        val uid = intent.getStringExtra("UID")
        mref = FirebaseDatabase.getInstance().reference

        getUserName(uid)

        btnOkUsername.setOnClickListener {
            val name = ed_username.text.toString()
            if (name.isNotEmpty()){
                updateUserName(name, uid)
            }
        }
    }

    private fun updateUserName(name: String?, uid: String?) {
        mref.child("Users").child(uid!!).child("name").setValue(name).addOnCompleteListener {
            if (it.isSuccessful){
                val intent = Intent(this@NameActivity, AvatarActivity::class.java)
                intent.putExtra("UID", uid)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getUserName(uid: String?) {
        mref.child("Users").child(uid!!).child("name").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                val defName = p0.value.toString().trim()
                ed_username.setText(defName)
            }

        })
    }
}
