package com.wo.orgs.extensions

import android.widget.ImageView
import coil.load
import com.wo.orgs.R

fun ImageView.tryRefreshImage(
    url: String? = null,
    fallback: Int = R.drawable.imagem_padrao)
{
    load(url) {
        fallback(fallback)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}