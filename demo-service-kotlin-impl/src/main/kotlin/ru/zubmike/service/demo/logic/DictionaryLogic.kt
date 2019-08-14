package ru.zubmike.service.demo.logic

import ru.zubmike.core.types.BasicDictItem
import ru.zubmike.core.types.DictItem
import ru.zubmike.service.demo.dao.PlanetarySystemDao

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