package xyz.luan.test.example1

private class Wrap1<A>

private fun <A : Any> wrap1(): Wrap1<A> = Wrap1()

private fun <A> infer(
    lambda: () -> Wrap1<A>,
): Wrap1<A> {
    return lambda()
}

fun main() {
    // wrap1() // Not enough information to infer type variable A
    infer { wrap1() } // Ok? A is inferred as Any
}
