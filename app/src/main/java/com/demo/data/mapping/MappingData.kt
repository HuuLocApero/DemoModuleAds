package com.demo.data.mapping

import com.demo.data.ui.Favourite
import com.demo.db.entity.FavouriteEntity
import kotlin.reflect.full.memberProperties

object MappingData {

    fun FavouriteEntity.toDataModel() = with(::Favourite) {
        val propertiesByName = FavouriteEntity::class.memberProperties.associateBy { it.name }
        callBy(parameters.associateWith { parameter ->
            when (parameter.name) {
                FavouriteEntity::id.name -> id
                else -> propertiesByName[parameter.name]?.get(this@toDataModel)
            }
        })
    }
}