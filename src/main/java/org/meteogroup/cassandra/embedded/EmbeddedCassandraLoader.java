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

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.service.CassandraDaemon;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class EmbeddedCassandraLoader {

  protected static CassandraDaemon cassandraDaemon;
  protected static String cassandraHost;
  protected static int cassandraNativePort;

  public static void setupCassandra() throws Exception {
    System.setProperty("cassandra.config.loader", EmbeddedConfigurationLoader.class.getCanonicalName());
    cassandraDaemon = new CassandraDaemon();
    cassandraDaemon.init(null);
    cassandraDaemon.start();

    cassandraHost = DatabaseDescriptor.getRpcAddress().getHostName();
    cassandraNativePort = DatabaseDescriptor.getNativeTransportPort();
  }

  public static void tearDownCassandra() throws IOException {
    cassandraHost = null;
    cassandraNativePort = -1;
    if (cassandraDaemon != null) {
      cassandraDaemon.stop();
    }

    EmbeddedConfigurationLoader.reset();
    for (String dataFileLocation : DatabaseDescriptor.getAllDataFileLocations()) {
      Files.walkFileTree(Paths.get(dataFileLocation), new Deleter());
    }
  }

  public static String getCassandraHost() {
    return cassandraHost;
  }

  public static int getCassandraNativePort() {
    return cassandraNativePort;
  }

  private static class Deleter implements FileVisitor<Path> {
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
      return FileVisitResult.TERMINATE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      Files.delete(dir);
      return FileVisitResult.CONTINUE;
    }
  }
}
