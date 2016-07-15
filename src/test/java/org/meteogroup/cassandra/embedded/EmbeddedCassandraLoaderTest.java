/*
Copyright © 2016 MeteoGroup Deutschland GmbH

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package org.meteogroup.cassandra.embedded;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedCassandraLoaderTest extends EmbeddedCassandraLoader {

  public static final Random RANDOM = new Random();
  public static final String TEST_KEYSPACE = "test_" + Long.toHexString(RANDOM.nextLong());
  public static final String TEST_TABLE = "test_" + Long.toHexString(RANDOM.nextLong());

  private Cluster cluster;
  private Session session;

  @BeforeClass
  public void setUpClass() throws Exception {
    setupCassandra();
    cluster = Cluster.builder()
        .withPort(getCassandraNativePort())
        .addContactPoint(getCassandraHost())
        .build();
    session = cluster.newSession();
  }

  @Test
  public void session_is_usable() throws Exception {
    final Select select = select().all().from("system", "local");

    final ResultSet resultSet = session.execute(select);

    assertThat(resultSet.one().getObject("host_id")).isInstanceOf(UUID.class);
  }

  @Test(dependsOnMethods = "session_is_usable")
  public void create_keyspace() throws Exception {

    session.execute("DROP KEYSPACE IF EXISTS \"" + TEST_KEYSPACE + "\"");
    session.execute("CREATE KEYSPACE \"" + TEST_KEYSPACE + "\" " +
                    "WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}");

    final KeyspaceMetadata keyspace = session.getCluster().getMetadata().getKeyspace(TEST_KEYSPACE);
    assertThat(keyspace).isNotNull();
  }

  @Test(dependsOnMethods = "create_keyspace")
  public void create_table() throws Exception {

    session.execute(SchemaBuilder.createTable(TEST_KEYSPACE, TEST_TABLE).addPartitionKey("ID", DataType.cint()));

    final KeyspaceMetadata keyspace = session.getCluster().getMetadata().getKeyspace(TEST_KEYSPACE);
    assertThat(keyspace.getTable(TEST_TABLE)).isNotNull();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() throws Exception {
    session.close();
    cluster.close();
    tearDownCassandra();
  }
}