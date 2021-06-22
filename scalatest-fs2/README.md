# ScalaTest fs2 Helpers

This module provides `inStream` and `inIO` methods (replacing the `in` method of `AsyncFlatTest`) that expect an [fs2 `Stream`](https://github.com/functional-streams-for-scala/fs2) or [cats-effect `IO`](https://github.com/typelevel/cats-effect), respectively. The “output” type should be ScalaTest `assertion`. This should increase type safety and prevent mistakes like tests accidentally passing because the `Stream` or `IO` is never actually executed.

## Stream example

```scala
class StreamExampleTest extends StreamSpec with org.scalatest.Matchers {

  "MyGreatCode" should "do something" inStream {
    fs2.Stream.emits(Seq(1, 2, 3))
      .map(_ should (be <= 3  and be > 0))
  }

}
```

The helper code will convert the `Stream[Pure, Assertion]` to an `IO[Assertion]`, which is then converted to a `Future[Assertion]` and handed back to ScalaTest. If the stream is empty, an exception will be raised.

## `IO` example

```scala
class IoExampleTest extends IOSpec with org.scalatest.Matchers {

  "MyGreatCode" should "do something" inIO {
    cats.effect.IO(1)
      .map(_ should be(1))
  }

}
```

The helper code will convert the `IO[Assertion]` to a `Future[Assertion]` and hand it to ScalaTest for evaluation.

## State Helpers

The `filterForSomeValue` and `filterForNoValue` methods can be used with a `StateT[F, ?, Option[?]]`, where you want to write a test that will deal with the `Some(_)` or `None` case, respectively. Writing a pattern match that applies to only one of the cases will cause a compiler warning, but the helper methods keep the warnings clean.

So instead of 

```scala
val stateMonadOutput = ("state", Option("output"))
for {
  (state, Some(output)) ← IO(stateMonadOutput)
} yield {
  state should be("state")
  output should be("output")
}
```

which would cause a warning indicating that `None` would cause a `MatchError`, use this instead:

```scala
val stateMonadOutput = ("state", Option("output"))
for {
  (state, output) ← filterForSomeValue(IO(stateMonadOutput))
} yield {
  state should be("state")
  output should be("output")
}
```

Note that the syntax above requires the [better-monadic-for](https://github.com/oleg-py/better-monadic-for) compiler plugin.

## Concurrency Helpers

`Pledge[F, A]` can be used as a pure-functional form of `scala.concurrent.Promise[A]`, and `completeThePledgeOnCancel` is a helper to construct a `Fiber[F, Unit]`, the cancelation of which will complete the given `Pledge[F, ?]`. This can be used to assert that a fiber obtained the normal way would be canceled if a timeout occurs, for example, without having to actually wait for a timeout.

## `IOErrorSpec` example
```scala
class IOErrorSpecExample extends IOSpec with org.scalatest.Matchers with IOErrorSpec {

  "MyGreatCode" should "throw an Exception" inIO {
    shouldThrowAn[Exception] {
        IO.raiseError(new Exception)
    }
  }

}
```

This helper trait will ensure an exception of the specified type is thrown inside the `IO`.
