import kotlin.reflect.KClass

class Wrap1<A: Any>(private val clazz: KClass<A>) {
  override fun toString(): String = clazz.toString()
}

inline fun <reified A: Any> wrap1(): Wrap1<A> = Wrap1(A::class)

inline fun <reified A: Any> infer(
    lambda: () -> Wrap1<A>,
): Wrap1<A> = lambda()

fun main() {
  // println(wrap1()) -- Not enough information to infer type variable A
  println(infer { wrap1() }) // -- Ok: class kotlin.Any
}
