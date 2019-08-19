package com.github.zubmike.service.demo.conf

import com.github.zubmike.service.demo.dao.PlanetarySystemDao
import com.github.zubmike.service.demo.dao.StarshipDao
import com.github.zubmike.service.demo.dao.ZoneDao
import com.github.zubmike.service.demo.dao.ZoneSpaceDao
import com.github.zubmike.service.demo.dao.db.PlanetarySystemDaoImpl
import com.github.zubmike.service.demo.dao.db.StarshipDaoImpl
import com.github.zubmike.service.demo.dao.db.ZoneDaoImpl
import com.github.zubmike.service.demo.dao.db.ZoneSpaceDaoImpl
import com.github.zubmike.service.demo.logic.DictionaryLogic
import com.github.zubmike.service.demo.logic.StarshipLogic
import com.github.zubmike.service.demo.logic.ZoneLogic
import com.github.zubmike.service.demo.utils.YamlUtils
import com.github.zubmike.service.managers.DbTransactionManager
import com.github.zubmike.service.managers.TransactionManager
import org.koin.dsl.module

const val defaultConfigurationFile = "config.yml"

val koinModule = module {

    val serviceProperties = YamlUtils.parse(defaultConfigurationFile, ServiceProperties::class.java)

    single { HibernateFactory.createSessionFactory(serviceProperties.dataBase!!) }

    single { PlanetarySystemDaoImpl(get()) as PlanetarySystemDao }
    single { StarshipDaoImpl(get()) as StarshipDao }
    single { ZoneDaoImpl(get()) as ZoneDao }
    single { ZoneSpaceDaoImpl(get()) as ZoneSpaceDao }

    single { DbTransactionManager(get()) as TransactionManager}

    single { DictionaryLogic(get()) }
    single { StarshipLogic(get(), get()) }
    single { ZoneLogic(get(), get(), get(), get()) }

}
