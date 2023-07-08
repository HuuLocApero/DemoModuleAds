package com.demo.ui.fragments.home

import com.demo.base.BaseViewModel
import com.demo.data.repository.impl.FavouriteRepositoryImplement
import com.demo.data.ui.Favourite
import com.demo.utils.listener.SingleLiveEvent
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var favouriteRepository: FavouriteRepositoryImplement

    val viewState = SingleLiveEvent<ViewState>()

    fun fetchData() {
        launchOnUITryCatch({
            val list = favouriteRepository.getListFavourite()
            viewState.value = ViewState.GetFavourite(list)
        }, {
            it.printStackTrace()
            viewState.value = ViewState.GetFavourite(arrayListOf())
        })
    }

    sealed class ViewState {
        class GetFavourite(val styles: ArrayList<Favourite>) : ViewState()
    }
}