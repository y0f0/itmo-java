commit c833da896fcb96947319ccf7821cfad9eabbf02e
Author: nikita <nik.pologov@gmail.com>
Date:   Fri Apr 1 19:02:45 2022 +0300

    add manifest class-path
==================================================
Compiling 1 Java sources
Tests: running
WARNING: A command line option has enabled the Security Manager
WARNING: The Security Manager is deprecated and will be removed in a future release
Running class info.kgeorgiy.java.advanced.implementor.InterfaceJarImplementorTest for info.kgeorgiy.ja.pologov.implementor.Implementor
=== Running test01_constructor
=== Running test09_encoding
test09_encoding\info\kgeorgiy\java\advanced\implementor\full\lang\??????InterfaceImpl.java:2: error: illegal character: '\u00b8'
public class ПриветInterfaceImpl implements info.kgeorgiy.java.advanced.implementor.full.lang.ПриветInterface {
                  ^
test09_encoding\info\kgeorgiy\java\advanced\implementor\full\lang\??????InterfaceImpl.java:2: error: illegal character: '\u00b2'
public class ПриветInterfaceImpl implements info.kgeorgiy.java.advanced.implementor.full.lang.ПриветInterface {
                    ^
test09_encoding\info\kgeorgiy\java\advanced\implementor\full\lang\??????InterfaceImpl.java:2: error: illegal character: '\u201a'
public class ПриветInterfaceImpl implements info.kgeorgiy.java.advanced.implementor.full.lang.ПриветInterface {
                        ^
test09_encoding\info\kgeorgiy\java\advanced\implementor\full\lang\??????InterfaceImpl.java:2: error: illegal character: '\u00b8'
public class ПриветInterfaceImpl implements info.kgeorgiy.java.advanced.implementor.full.lang.ПриветInterface {
                                                                                                         ^
test09_encoding\info\kgeorgiy\java\advanced\implementor\full\lang\??????InterfaceImpl.java:2: error: illegal character: '\u00b2'
public class ПриветInterfaceImpl implements info.kgeorgiy.java.advanced.implementor.full.lang.ПриветInterface {
                                                                                                           ^
test09_encoding\info\kgeorgiy\java\advanced\implementor\full\lang\??????InterfaceImpl.java:2: error: illegal character: '\u201a'
public class ПриветInterfaceImpl implements info.kgeorgiy.java.advanced.implementor.full.lang.ПриветInterface {
                                                                                                               ^
6 errors
=== Running test03_standardInterfaces
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.AccessibleImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.AccessibleActionImpl
Note: test03_standardInterfaces\info\kgeorgiy\java\advanced\implementor\full\interfaces\standard\SDeprecatedImpl.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.SDeprecatedImpl
Note: C:\Users\kWX1085047\java-advanced-private\test\__local\build\test03_standardInterfaces\info\kgeorgiy\java\advanced\implementor\full\interfaces\standard\SDeprecatedImpl.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.AccessibleImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.AccessibleActionImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.SDeprecatedImpl
=== Running test07_duplicateClasses
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.ProxiesImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.ProxiesImpl
=== Running test04_extendedInterfaces
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.DescriptorImpl
Note: test04_extendedInterfaces\info\kgeorgiy\java\advanced\implementor\full\interfaces\standard\CachedRowSetImpl.java uses or overrides a deprecated API.
Note: Recompile with -Xlint:deprecation for details.
Note: test04_extendedInterfaces\info\kgeorgiy\java\advanced\implementor\full\interfaces\standard\CachedRowSetImpl.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.CachedRowSetImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.DataInputImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.DataOutputImpl
	Loading class info.kgeorgiy.java.advanced.implementor.basic.classes.standard.LoggerImpl
Note: C:\Users\kWX1085047\java-advanced-private\test\__local\build\test04_extendedInterfaces\info\kgeorgiy\java\advanced\implementor\full\interfaces\standard\CachedRowSetImpl.java uses or overrides a deprecated API.
Note: Recompile with -Xlint:deprecation for details.
Note: C:\Users\kWX1085047\java-advanced-private\test\__local\build\test04_extendedInterfaces\info\kgeorgiy\java\advanced\implementor\full\interfaces\standard\CachedRowSetImpl.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.DescriptorImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.CachedRowSetImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.DataInputImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.DataOutputImpl
	Loading class info.kgeorgiy.java.advanced.implementor.basic.classes.standard.LoggerImpl
=== Running test08_nestedInterfaces
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.PublicInterfaceImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.PackagePrivateInterfaceImpl
			class info.kgeorgiy.java.advanced.implementor.full.interfaces.PackagePrivateInterfaceImpl cannot access its superinterface info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$PackagePrivateInterface (info.kgeorgiy.java.advanced.implementor.full.interfaces.PackagePrivateInterfaceImpl is in unnamed module of loader java.net.URLClassLoader @4593ff34; info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$PackagePrivateInterface is in module info.kgeorgiy.java.advanced.implementor of loader 'app')
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.InheritedInterfaceImpl
			class info.kgeorgiy.java.advanced.implementor.full.interfaces.InheritedInterfaceImpl cannot access its superinterface info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$InheritedInterface (info.kgeorgiy.java.advanced.implementor.full.interfaces.InheritedInterfaceImpl is in unnamed module of loader java.net.URLClassLoader @5fd9b663; info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$InheritedInterface is in module info.kgeorgiy.java.advanced.implementor of loader 'app')
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.ProtectedInterfaceImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.PublicInterfaceImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.PackagePrivateInterfaceImpl
			class info.kgeorgiy.java.advanced.implementor.full.interfaces.PackagePrivateInterfaceImpl cannot access its superinterface info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$PackagePrivateInterface (info.kgeorgiy.java.advanced.implementor.full.interfaces.PackagePrivateInterfaceImpl is in unnamed module of loader java.net.URLClassLoader @60e9df3c; info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$PackagePrivateInterface is in module info.kgeorgiy.java.advanced.implementor of loader 'app')
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.InheritedInterfaceImpl
			class info.kgeorgiy.java.advanced.implementor.full.interfaces.InheritedInterfaceImpl cannot access its superinterface info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$InheritedInterface (info.kgeorgiy.java.advanced.implementor.full.interfaces.InheritedInterfaceImpl is in unnamed module of loader java.net.URLClassLoader @60e9df3c; info.kgeorgiy.java.advanced.implementor.full.interfaces.Interfaces$InheritedInterface is in module info.kgeorgiy.java.advanced.implementor of loader 'app')
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.ProtectedInterfaceImpl
=== Running test06_java8Interfaces
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.InterfaceWithStaticMethodImpl
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.InterfaceWithDefaultMethodImpl
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.InterfaceWithStaticMethodImpl
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.InterfaceWithDefaultMethodImpl
=== Running test05_standardNonInterfaces
=== Running test02_methodlessInterfaces
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.RandomAccessImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.InterfaceWithoutMethodsImpl
	Loading class info.kgeorgiy.java.advanced.implementor.basic.interfaces.standard.RandomAccessImpl
	Loading class info.kgeorgiy.java.advanced.implementor.full.interfaces.InterfaceWithoutMethodsImpl
Test test09_encoding failed: Error implementing interface info.kgeorgiy.java.advanced.implementor.full.lang.??????Interface
java.lang.AssertionError: Error implementing interface info.kgeorgiy.java.advanced.implementor.full.lang.??????Interface
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.BaseImplementorTest.implement(BaseImplementorTest.java:98)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.BaseImplementorTest.test(BaseImplementorTest.java:154)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.BaseImplementorTest.test(BaseImplementorTest.java:176)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.InterfaceJarImplementorTest.test09_encoding(InterfaceJarImplementorTest.java:31)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at junit@4.11/org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
	at junit@4.11/org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at junit@4.11/org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
	at junit@4.11/org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at junit@4.11/org.junit.rules.TestWatcher$1.evaluate(TestWatcher.java:55)
	at junit@4.11/org.junit.rules.RunRules.evaluate(RunRules.java:20)
	at junit@4.11/org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
	at junit@4.11/org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:127)
	at junit@4.11/org.junit.runners.Suite.runChild(Suite.java:26)
	at junit@4.11/org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
	at junit@4.11/org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
	at junit@4.11/org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
	at junit@4.11/org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
	at junit@4.11/org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
	at junit@4.11/org.junit.runners.ParentRunner.run(ParentRunner.java:309)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:160)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:138)
	at junit@4.11/org.junit.runner.JUnitCore.run(JUnitCore.java:117)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:55)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.lambda$add$0(BaseTester.java:95)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.test(BaseTester.java:48)
	at info.kgeorgiy.java.advanced.base/info.kgeorgiy.java.advanced.base.BaseTester.run(BaseTester.java:39)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.Tester.main(Tester.java:21)
Caused by: info.kgeorgiy.java.advanced.implementor.ImplerException: Error: can not compiler class
	at info.kgeorgiy.ja.pologov.implementor.Implementor.compile(Implementor.java:360)
	at info.kgeorgiy.ja.pologov.implementor.Implementor.implementJar(Implementor.java:333)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.InterfaceJarImplementorTest.implementJar(InterfaceJarImplementorTest.java:42)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.InterfaceJarImplementorTest.implement(InterfaceJarImplementorTest.java:37)
	at info.kgeorgiy.java.advanced.implementor/info.kgeorgiy.java.advanced.implementor.BaseImplementorTest.implement(BaseImplementorTest.java:91)
	... 38 more
ERROR: Tests: failed
