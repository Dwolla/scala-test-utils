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
