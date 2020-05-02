package com.ibnuputra.mafia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ibnuputra.mafia.Model.UserModel
import com.ibnuputra.mafia.R
import com.ibnuputra.mafia.Util.LoadImages
import kotlinx.android.synthetic.main.player_list.view.*


class UserListAdapter (
    private val result: List<UserModel>,
    private val context: Context
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.player_list, parent, false))
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = result[position]
        holder.name?.text = list.name
        LoadImages(list.avatar, holder.avatar)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name: TextView? = view.tv_name
        val avatar: ImageView? = view.pic_avatar
    }

}