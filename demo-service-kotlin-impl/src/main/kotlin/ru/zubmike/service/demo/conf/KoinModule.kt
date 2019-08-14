package ru.zubmike.service.demo.conf

import org.koin.dsl.module
import ru.zubmike.service.demo.dao.PlanetarySystemDao
import ru.zubmike.service.demo.dao.StarshipDao
import ru.zubmike.service.demo.dao.ZoneDao
import ru.zubmike.service.demo.dao.ZoneSpaceDao
import ru.zubmike.service.demo.dao.db.PlanetarySystemDaoImpl
import ru.zubmike.service.demo.dao.db.StarshipDaoImpl
import ru.zubmike.service.demo.dao.db.ZoneDaoImpl
import ru.zubmike.service.demo.dao.db.ZoneSpaceDaoImpl
import ru.zubmike.service.demo.logic.DictionaryLogic
import ru.zubmike.service.demo.logic.StarshipLogic
import ru.zubmike.service.demo.logic.ZoneLogic
import ru.zubmike.service.demo.utils.YamlUtils
import ru.zubmike.service.managers.DbTransactionManager
import ru.zubmike.service.managers.TransactionManager

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
