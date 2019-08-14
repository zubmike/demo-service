package ru.zubmike.service.demo.utis

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

object MockitoExt : Mockito() {

    fun <T> whenever(methodCall: T): OngoingStubbing<T> = `when`(methodCall)

    fun <T : Any> safeEq(value: T): T = eq(value) ?: value

}