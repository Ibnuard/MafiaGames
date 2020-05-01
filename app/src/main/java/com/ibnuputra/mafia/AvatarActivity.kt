package com.ibnuputra.mafia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.ibnuputra.mafia.Util.LoadImages
import kotlinx.android.synthetic.main.activity_avatar.*

class AvatarActivity : AppCompatActivity() {
    private lateinit var mref: DatabaseReference
    private var img:Int = 0
    private var avatar: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)

        mref = FirebaseDatabase.getInstance().reference
        val uid = intent.getStringExtra("UID")
        getAvatar(uid)

        btnOkAvatar.setOnClickListener {
            mref.child("Users").child(uid).child("avatar").setValue(avatar).addOnCompleteListener {
                if (it.isSuccessful){
                    val intent = Intent(this@AvatarActivity, RoomActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        pic0.setOnClickListener {
            img = 1
            changeImage(img)
        }
        pic1.setOnClickListener {
            img = 2
            changeImage(img)
            avatar = "https://i.ibb.co/qmZ6DTP/avatar1-1.png"
        }
        pic2.setOnClickListener {
            img = 3
            changeImage(img)
            avatar = "https://i.ibb.co/zPf8SVQ/avatar2-1.png"
        }
        pic3.setOnClickListener {
            img = 4
            changeImage(img)
            avatar = "https://i.ibb.co/HT5Lpvc/avatar3-1.png"
        }
        pic4.setOnClickListener {
            img = 5
            changeImage(img)
            avatar = "https://i.ibb.co/PNhCvgc/avatar4-1.png"
        }
        pic5.setOnClickListener {
            img = 6
            changeImage(img)
            avatar = "https://i.ibb.co/yqjvBgX/avatar06-1.png"
        }
    }

    private fun changeImage(img: Int) {
        when(img){
            1 -> LoadImages(avatar, pic)
            2 -> pic.setImageResource(R.drawable.ic_avatar1)
            3 -> pic.setImageResource(R.drawable.ic_avatar2)
            4 -> pic.setImageResource(R.drawable.ic_avatar3)
            5 -> pic.setImageResource(R.drawable.ic_avatar4)
            6 -> pic.setImageResource(R.drawable.ic_avatar5)
        }

    }

    private fun getAvatar(uid: String?) {
        mref.child("Users").child(uid!!).child("avatar").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                avatar = p0.value.toString()
                LoadImages(avatar, pic)
                LoadImages(avatar, pic0)
            }

        })
    }
}
