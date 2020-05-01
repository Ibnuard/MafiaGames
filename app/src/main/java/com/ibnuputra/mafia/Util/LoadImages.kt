package com.ibnuputra.mafia.Util

import android.widget.ImageView
import com.squareup.picasso.Picasso

class LoadImages(url: String?, image: ImageView?) {
    init {
        Picasso.get().load(url).into(image)
    }
}