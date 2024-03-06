class Wrap2<A, B>

class ConcreteType
fun <A: Any> wrap2(): Wrap2<A, ConcreteType> = Wrap2()

class DSL<B> {
  fun <A> infer(param: Wrap2<A, B>): Wrap2<A, B> = param
}

fun <B> dsl(
    lambda: DSL<B>.() -> Wrap2<*, B>,
) = DSL<B>().lambda()

fun main() {
  dsl {
    infer(wrap2<String>())
  }
}
