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

import org.apache.cassandra.config.Config;
import org.apache.cassandra.config.ParameterizedClass;
import org.apache.cassandra.dht.ByteOrderedPartitioner;
import org.apache.cassandra.locator.SimpleSeedProvider;
import org.apache.cassandra.locator.SimpleSnitch;
import org.apache.cassandra.scheduler.RoundRobinScheduler;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.Collections;

import static java.nio.file.Files.createTempDirectory;
import static org.apache.cassandra.config.Config.CommitLogSync.batch;
import static org.apache.cassandra.config.Config.MemtableAllocationType.heap_buffers;

public class EmbeddedConfig extends Config {

  protected Path datadir;

  public EmbeddedConfig() {
    try {
      datadir = createTempDirectory("cassandra");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    setupFreePorts();
    memtable_allocation_type = heap_buffers;
    commitlog_sync = batch;
    commitlog_sync_batch_window_in_ms = 1.0;
    commitlog_directory = datadir.resolve("commitlog").toString();
    hints_directory = datadir.resolve("hints").toString();
    partitioner = ByteOrderedPartitioner.class.getCanonicalName();
    listen_address = "127.0.0.1";
    start_native_transport = true;
    saved_caches_directory = datadir.resolve("saved_caches").toString();
    data_file_directories = new String[] {datadir.toString()};
    seed_provider = new ParameterizedClass(SimpleSeedProvider.class.getCanonicalName(), Collections.singletonMap("seeds", "127.0.0.1"));
    endpoint_snitch = SimpleSnitch.class.getCanonicalName();
    dynamic_snitch = true;
    request_scheduler = RoundRobinScheduler.class.getCanonicalName();
    request_scheduler_id = RequestSchedulerId.keyspace;
    incremental_backups = true;
    concurrent_compactors = 4;
    row_cache_class_name = "org.apache.cassandra.cache.OHCProvider";
    row_cache_size_in_mb = 64;
    enable_user_defined_functions = true;
    enable_scripted_user_defined_functions = true;
  }

  private void setupFreePorts() {
    try (
        final ServerSocket rpcSocket = new ServerSocket(0);
        final ServerSocket storageSocket = new ServerSocket(0);
        final ServerSocket storageSSLSocket = new ServerSocket(0);
        final ServerSocket nativeTransportSocket = new ServerSocket(0);
    ) {
      rpc_port = rpcSocket.getLocalPort();
      storage_port = storageSocket.getLocalPort();
      ssl_storage_port = storageSSLSocket.getLocalPort();
      native_transport_port = nativeTransportSocket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
