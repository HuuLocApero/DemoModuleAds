package com.demo.data.repository

import com.demo.data.ui.Favourite


interface FavouriteRepository {

    suspend fun getListFavourite(): ArrayList<Favourite>
}