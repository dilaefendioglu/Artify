package com.dilaefendioglu.artify.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

    fun ImageView.setImageUrl(url: String?) {
            Picasso.get()
                .load(url)
                .into(this)

    }
