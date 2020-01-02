package com.dwolla.testutils.mocking

import java.io.File

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class WithBehaviorSpec extends Specification with Mockito with WithBehaviorMocking {

  "Mocks" should {
    "be extensible with behavior" >> {
      val mockFile = mock[File] withBehavior { file => file.getCanonicalPath returns "/"; ()}

      mockFile.getCanonicalPath must_== "/"
    }
  }

}
