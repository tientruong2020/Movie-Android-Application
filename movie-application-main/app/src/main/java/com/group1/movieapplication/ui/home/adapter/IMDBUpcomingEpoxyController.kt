package com.group1.movieapplication.ui.home.adapter

import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.epoxy.carousel
import com.group1.movieapplication.UpcomingBindingModel_
import com.group1.movieapplication.model.upcoming.UpComingItem

class IMDBUpcomingEpoxyController(
    private val upcomingItemOnClickListener: UpcomingItemOnClickListener
) : TypedEpoxyController<List<UpComingItem>>() {
    override fun buildModels(data: List<UpComingItem>?) {
        val itemModels = mutableListOf<UpcomingBindingModel_>()
        data?.forEach {
            itemModels.add(
                UpcomingBindingModel_()
                    .id(it.id)
                    .item(it)
                    .onClickListener(upcomingItemOnClickListener)
            )
        }

        carousel {
            id("")
            numViewsToShowOnScreen(3f)
            models(itemModels)
        }
    }
}