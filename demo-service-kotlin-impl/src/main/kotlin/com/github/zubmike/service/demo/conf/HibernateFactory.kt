package com.github.zubmike.service.demo.conf

import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import com.github.zubmike.service.demo.types.PlanetarySystem
import com.github.zubmike.service.demo.types.Starship
import com.github.zubmike.service.demo.types.Zone
import com.github.zubmike.service.demo.types.ZoneSpace
import com.github.zubmike.service.conf.DataBaseProperties

object HibernateFactory {

    private val entities: List<Class<*>> = listOf(
            Zone::class.java,
            ZoneSpace::class.java,
            Starship::class.java,
            PlanetarySystem::class.java)

    fun createSessionFactory(dataBaseProperties: DataBaseProperties): SessionFactory {
        val hibernateConfiguration = createConfiguration(dataBaseProperties, entities.toList())
        val builder = StandardServiceRegistryBuilder()
                .applySettings(hibernateConfiguration.properties)
        return hibernateConfiguration.buildSessionFactory(builder.build())
    }

    private fun createConfiguration(dataBaseProperties: DataBaseProperties, entities: Collection<Class<*>>): Configuration {
        val configuration = Configuration()
        configuration.setProperty("hibernate.connection.driver_class", dataBaseProperties.driverClass)
        configuration.setProperty("hibernate.dialect", dataBaseProperties.dialect)
        configuration.setProperty("hibernate.connection.url", dataBaseProperties.url)
        if (dataBaseProperties.user != null) {
            configuration.setProperty("hibernate.connection.username", dataBaseProperties.user)
        }
        if (dataBaseProperties.password != null) {
            configuration.setProperty("hibernate.connection.password", dataBaseProperties.password)
        }
        dataBaseProperties.properties.forEach { (propertyName, value) -> configuration.setProperty(propertyName, value) }
        for (entity in entities) {
            configuration.addAnnotatedClass(entity)
        }
        return configuration
    }

}