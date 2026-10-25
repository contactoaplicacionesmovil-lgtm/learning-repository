package com.heroes.app.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Hero(
    @StringRes val titulo: Int,
    @StringRes val descripcion: Int,
    @DrawableRes val imagen: Int
)