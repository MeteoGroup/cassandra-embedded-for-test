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
