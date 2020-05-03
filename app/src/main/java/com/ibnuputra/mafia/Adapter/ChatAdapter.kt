package com.ibnuputra.mafia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ibnuputra.mafia.Model.ChatModel
import com.ibnuputra.mafia.R
import com.ibnuputra.mafia.Util.LoadImages

class ChatAdapter(private val result: List<ChatModel>,
                  private val context: Context
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bubble_chat, parent, false))
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = result[position]
        holder.name.text = list.name
        holder.msg.text = list.chat
        LoadImages(list.pic, holder.img)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.chatName)
        val msg: TextView = view.findViewById(R.id.chatText)
        val img: ImageView = view.findViewById(R.id.pic)
    }
}