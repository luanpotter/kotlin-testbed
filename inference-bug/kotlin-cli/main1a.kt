class Wrap1<A>

fun <A : Any> wrap1(): Wrap1<A> = Wrap1()

fun <A> infer(
    lambda: () -> Wrap1<A>,
): Wrap1<A> {
  return lambda()
}

fun main() {
  println(wrap1()) // Not enough information to infer type variable A
}
