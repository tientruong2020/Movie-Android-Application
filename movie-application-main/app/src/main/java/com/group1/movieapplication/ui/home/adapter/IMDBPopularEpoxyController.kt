package com.group1.movieapplication.ui.home.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.carousel
import com.group1.movieapplication.PopularBindingModel_
import com.group1.movieapplication.model.popular.PopularItem

class IMDBPopularEpoxyController(
    private val itemOnClickListener: PopularItemOnClickListener
) : TypedEpoxyController<List<PopularItem>>() {
    override fun buildModels(data: List<PopularItem>?) {
        val itemModels = mutableListOf<PopularBindingModel_>()
        data?.forEach {
            itemModels.add(
                PopularBindingModel_()
                    .id(it.id)
                    .item(it)
                    .onClickListener(itemOnClickListener)
            )
        }

        carousel {
            id("")
            numViewsToShowOnScreen(3f)
            models(itemModels)
        }
    }

}