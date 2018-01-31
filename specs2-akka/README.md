# Specs2 Akka Test Helpers

### `AkkaTestKitSpecs2Support`

Use the `AkkaTestKitSpecs2Support` class for the test case’s scope to make an `ActorSystem` available with an optional `Config`. The `ActorSystem` will be shutdown automatically after the test run.

    class TestSpec extends Specification {
      trait Setup extends AkkaTestKitSpecs2Support {
        …
      }

      "test" should {
        "have access to an ActorSystem" in new Setup {
          …
        }
      }
    }

Note that due to the `DelayedInit` trait on `After` (which is mixed into `AkkaTestKitSpecs2Support`), users of `AkkaTestKitSpecs2Support` should make sure to extend it as a `trait` and not a `class`, or they risk race conditions due to `after` being run twice. (See https://stackoverflow.com/questions/21154941/specs2-after-method-runs-before-the-example/21223797#21223797.)

