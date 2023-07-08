package com.demo.data.mapping

import com.demo.data.ui.Favourite
import com.demo.db.entity.FavouriteEntity
import kotlin.reflect.full.memberProperties

object MappingEntity {

    fun Favourite.toDataEntity() = with(::FavouriteEntity) {
        val propertiesByName = Favourite::class.memberProperties.associateBy { it.name }
        callBy(parameters.associate { parameter ->
            parameter to when (parameter.name) {
                Favourite::id.name -> id
                else -> propertiesByName[parameter.name]?.get(this@toDataEntity)
            }
        })
    }
}