package com.github.zubmike.service.demo.logic

import com.github.zubmike.core.types.BasicDictItem
import com.github.zubmike.core.types.DictItem
import com.github.zubmike.service.demo.dao.PlanetarySystemDao

class DictionaryLogic(
        private val planetarySystemDao: PlanetarySystemDao) {

    fun getPlanetarySystems(): List<BasicDictItem> {
        return planetarySystemDao.all
                .map { createBasicDictItem(it) }
    }

    private fun createBasicDictItem(item: DictItem<Int>): BasicDictItem {
        return BasicDictItem(item.id, item.name)
    }

}