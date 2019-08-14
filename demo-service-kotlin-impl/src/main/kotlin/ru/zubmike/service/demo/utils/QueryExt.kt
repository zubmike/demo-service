package ru.zubmike.service.demo.utils

import org.hibernate.query.Query

fun <T : Any> Query<T>.uniqueResultNullable(): T? = this.uniqueResultOptional().orElse(null)