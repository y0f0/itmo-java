commit aec75c589ab732fad1dbf3301f408a5a44d2fe54
Author: nikita <nik.pologov@gmail.com>
Date:   Thu May 5 15:17:40 2022 +0300

    add 9
==================================================
Compiling 2 Java sources
Tests: running
WARNING: A command line option has enabled the Security Manager
WARNING: The Security Manager is deprecated and will be removed in a future release
Running class info.kgeorgiy.java.advanced.hello.HelloClientTest for info.kgeorgiy.ja.pologov.hello.HelloUDPClient
=== Running test01_singleRequest
Exception in thread "Thread-0" java.lang.AssertionError: Invalid or unexpected request info.kgeorgiy.java.advanced.hello.HelloClientTest0_0 expected:<1> but was:<0>
at junit@4.11/org.junit.Assert.fail(Assert.java:88)
at junit@4.11/org.junit.Assert.failNotEquals(Assert.java:743)
at junit@4.11/org.junit.Assert.assertEquals(Assert.java:118)
at junit@4.11/org.junit.Assert.assertEquals(Assert.java:555)
at info.kgeorgiy.java.advanced.hello/info.kgeorgiy.java.advanced.hello.Util.lambda$server$1(Util.java:159)
at java.base/java.lang.Thread.run(Thread.java:833)
Test finished in 0.061s
=== Running test02_sequence
Test finished in 0.060s
Socket closed
=== Running test03_singleWithFailures
Test finished in 0.004s
Socket closed
=== Running test04_sequenceWithFailures
Test finished in 0.021s
Socket closed
=== Running test05_singleMultithreaded
Exception in thread "Thread-4" java.lang.AssertionError: Invalid or unexpected request info.kgeorgiy.java.advanced.hello.HelloClientTest2_0 expected:<1> but was:<0>
at junit@4.11/org.junit.Assert.fail(Assert.java:88)
at junit@4.11/org.junit.Assert.failNotEquals(Assert.java:743)
at junit@4.11/org.junit.Assert.assertEquals(Assert.java:118)
at junit@4.11/org.junit.Assert.assertEquals(Assert.java:555)
at info.kgeorgiy.java.advanced.hello/info.kgeorgiy.java.advanced.hello.Util.lambda$server$1(Util.java:159)
at java.base/java.lang.Thread.run(Thread.java:833)
