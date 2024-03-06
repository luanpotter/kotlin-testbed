# A Small Bug in Paradise

It is no secret that Kotlin has become one of my favorite languages of all times recently.

In playing around I recently found what I believe might be a bug with the compiler itself.

Before we get to the crux of the matter, let's go through a small journey.

Let's define a `Wrap1` class which is just our traditional "Box", the quintessential Generics example.
Alongside it, we define a function `wrap1` which receives a generic type `A` and creates a `Wrap1` with that type.

```kotlin
class Wrap1<A>

fun <A> wrap1(): Wrap1<A> = Wrap1()
```

Now Kotlin has really powerful type inference for Generics. Which is awesome. But sometimes it is _impossible_ to infer a type.
For example if we call:

```kotlin
fun main() {
  println(wrap1())
}
```

That does not compile; we get:

> Not enough information to infer type variable A

That is not a flaw or a bug. That is expected and intentional. The problem is not with Kotlin at all; we have not provided any way for it to infer the type `A`. It shouldn't try to guess; and it doesn't. Great.

However, we are able to induce Kotlin into mistakenly "inferring" this type. We need two things. First we need to add any boundary to the type `A`. We can add that to both `Wrap1` and `wrap1`; but adding only to the function suffices:

```kotlin
fun <A: Any> wrap1(): Wrap1<A> = Wrap1()
```

Note that this does not change the previous example (it still fails to compile).

Then, we define a trickster function which we can call `infer`:

```kotlin
fun <A> infer(
    lambda: () -> Wrap1<A>,
): Wrap1<A> {
  return lambda()
}
```

Note that the `infer` function also does not specify the type `A`. Which means that it is still unspecified. However, that "tricks" Kotlin into incorrectly "inferring" `A` to be `Any` (the upper bound we added to `wrap1`).

```kotlin
fun main() {
  // println(wrap1()) // Not enough information to infer type variable A
  println(infer { wrap1() }) // Ok (?)
}
```

In fact if we use some reflection we can see that Kotlin is indeed inferring `A` to be `Any`:

```kotlin
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
```

Or whatever the upper bound we specify on `wrap1` is. Note that this more complex example is just to show what is being inferred. Reflection is not relevant at all to the problem.

That might seem as Kotlin "trying its best" to help us infer, but I believe it is a problem. It should really only try to infer when information is available. In this case the function `infer` adds absolutely _no information whatsoever_ about the type `A`; it is no different than calling `wrap1` directly.

That is not a "bug", you might say. Not yet. So let's get deeper. Let's introduce `Wrap2`:

```kotlin
class Wrap2<A, B>
```

And with it our `wrap2`; but this time we will take only one of the parameters generically, and specify the second:

```kotlin
class ConcreteType
fun <A : Any> wrap2(): Wrap2<A, ConcreteType> = Wrap2()
```

Let's also define a "DSL" function that takes a lambda with receiver:

```kotlin
private class DSL<B> {
    fun <A> infer(param: Wrap2<A, B>): Wrap2<A, B> = param
}

private fun <B> dsl(
    lambda: DSL<B>.() -> Wrap2<*, B>,
) = DSL<B>().lambda()
```

And now for the real bug. This will actually crash your compiler. Use it at your own risk!

```kotlin
fun main() {
    dsl { infer(wrap2<String>()) } // Ok; A is specified

    dsl { infer(wrap2()) } // Not ok! and will crash the compiler!
}
```

The error message is not very helpful:

> e: Could not load module <Error module>

The issue seems to be that Kotlin needs to use `wrap2` to specify the type of `B` to `ConcreteType`; but it never got to specify properly the type of `A` due to issues we explored before.