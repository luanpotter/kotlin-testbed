package xyz.luan.test.example2

import kotlin.reflect.KClass

private class Wrap1<A : Any>(private val clazz: KClass<A>) {
    override fun toString(): String = clazz.toString()
}

private inline fun <reified A : Any> wrap1(): Wrap1<A> = Wrap1(A::class)

private inline fun <reified A : Any> infer(
    lambda: () -> Wrap1<A>,
): Wrap1<A> = lambda()

fun main() {
    // println(wrap1()) -- Not enough information to infer type variable A
    println(infer { wrap1() }) // -- Ok: class kotlin.Any
}
