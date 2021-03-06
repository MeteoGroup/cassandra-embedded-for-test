Embedded Cassandra for Tests
============================

Features
--------

- runs an embedded cassandra using a temporary directory for data files
- removes all temporary data on shutdown
- Easily extensible `EmbeddedConfig` class for YAML-free configuration of the
  embedded cassandra.


How to build
------------

Just run the maven build
```
mvn clean verify
```


Using it in tests
-----------------

**Warning: Currently it's not advisable to use the `EmbeddedCassandraLoader`
for concurrent tests.**

**Warning: Under windows `tearDownCassandra()` will terminate the JVM. See
[Running under Windows](#running-under-windows) for futher details.**

TestNG example:

```java
public class MyTest {

  private Cluster cluster;
  private Session session;

  @BeforeClass
  public void setUpClass() throws Exception {
    EmbeddedCassandraLoader.setupCassandra();
    cluster = Cluster.builder()
        .withPort(getCassandraNativePort())
        .addContactPoint(getCassandraHost())
        .build();
    session = cluster.newSession();
  }

  @Test
  public void actual_test() throws Exception {
    session.execute(select().all().from("my_table"));
    // ...
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    session.close();
    cluster.close();
    EmbeddedCassandraLoader.tearDownCassandra();
  }
}
```

Tweaking configuration is done by extending `EmbeddedConfig` and registering
your class with the `EmbeddedConfigurationLoader`:

```java
class MyTestConfig extends EmbeddedConfig{
  MyTestConfig() {
    // Custom configuration goes here.
  }
}

public class MyTest {
  @BeforeClass
  public void setUpClass() throws Exception {
     EmbeddedConfigurationLoader.configSupplier = MyTestConfig::new;
     EmbeddedCassandraLoader.setupCassandra();
     //...
  }
}
```


Running under Windows
---------------------

Under Windows Cassandra terminates the whole JVM when stopping the Cassandra
daemon. That means running `tearDownCassandra()` will kill the JVM, something
that at least TestNG is not happy about.

As a workaround tear down can be skipped, leaving the daemon running.
Consequentially only one test using Cassandra can be run in single test suite
and files created by Cassandra during tests will not be removed. In a mixed
environment the tear down can be called conditionally:

```
  ...
  
  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    session.close();
    cluster.close();
    if (!FBUtilities.isWindows()) { // cassandra does the same check to decide wether to kill the JVM or not
      EmbeddedCassandraLoader.tearDownCassandra();
    }
  }

  ...
```

There are plans to allow runing Cassandra in a separate process which would
fix this issue. 


License
-------

Copyright © 2016 MeteoGroup Deutschland GmbH

Licensed under the Apache License, Version 2.0 (the "License"); you may not use
any file from this repository except in compliance with the License. You may
obtain a copy of the License at

  <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed
under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
