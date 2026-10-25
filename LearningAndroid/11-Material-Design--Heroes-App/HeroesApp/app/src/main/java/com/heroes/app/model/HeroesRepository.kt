package com.heroes.app.model

import com.heroes.app.R

object HeroesRepository {
    val heroes = listOf(
        Hero(
            titulo = R.string.hero1,
            descripcion = R.string.description1,
            imagen = R.drawable.android_superhero1
        ),
        Hero(
            titulo = R.string.hero2,
            descripcion = R.string.description2,
            imagen = R.drawable.android_superhero2
        ),
        Hero(
            titulo = R.string.hero3,
            descripcion = R.string.description3,
            imagen = R.drawable.android_superhero3
        ),
        Hero(
            titulo = R.string.hero4,
            descripcion = R.string.description4,
            imagen = R.drawable.android_superhero4
        ),
        Hero(
            titulo = R.string.hero5,
            descripcion = R.string.description5,
            imagen = R.drawable.android_superhero5
        ),
        Hero(
            titulo = R.string.hero6,
            descripcion = R.string.description6,
            imagen = R.drawable.android_superhero6
        )
    )
}