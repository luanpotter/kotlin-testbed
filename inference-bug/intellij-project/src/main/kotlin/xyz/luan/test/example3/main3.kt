private class Wrap2<A, B>

private class ConcreteType

private fun <A : Any> wrap2(): Wrap2<A, ConcreteType> = Wrap2()

private class DSL<B> {
    fun <A> infer(param: Wrap2<A, B>): Wrap2<A, B> = param
}

private fun <B> dsl(
    lambda: DSL<B>.() -> Wrap2<*, B>,
) = DSL<B>().lambda()


fun main() {
//    dsl { infer(wrap2<String>()) } // Ok; A is specified
    dsl { infer(wrap2()) } // will crash the compiler!
}
