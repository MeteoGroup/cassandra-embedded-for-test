package org.meteogroup.cassandra.embedded;

import sun.reflect.CallerSensitive;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.Objects;

class SecurityManagerDecorator extends SecurityManager {

  final SecurityManager delegate;

  SecurityManagerDecorator(SecurityManager securityManager) {
    delegate = securityManager;
  }

  @Override
  @Deprecated
  public boolean getInCheck() {
    return Objects.nonNull(delegate) ? delegate.getInCheck() : super.getInCheck();
  }

  @Override
  public Object getSecurityContext() {
    return Objects.nonNull(delegate) ? delegate.getSecurityContext() : super.getSecurityContext();
  }

  @Override
  public void checkPermission(Permission perm) {
    if (Objects.nonNull(delegate)) {
      delegate.checkPermission(perm);
    } else {
      super.checkPermission(perm);
    }
  }

  @Override
  public void checkPermission(Permission perm, Object context) {
    if (Objects.nonNull(delegate)) {
      delegate.checkPermission(perm, context);
    } else {
      super.checkPermission(perm, context);
    }
  }

  @Override
  public void checkCreateClassLoader() {
    if (Objects.nonNull(delegate)) {
      delegate.checkCreateClassLoader();
    } else {
      super.checkCreateClassLoader();
    }
  }

  @Override
  public void checkAccess(Thread t) {
    if (Objects.nonNull(delegate)) {
      delegate.checkAccess(t);
    } else {
      super.checkAccess(t);
    }
  }

  @Override
  public void checkAccess(ThreadGroup g) {
    if (Objects.nonNull(delegate)) {
      delegate.checkAccess(g);
    } else {
      super.checkAccess(g);
    }
  }

  @Override
  public void checkExit(int status) {
    if (Objects.nonNull(delegate)) {
      delegate.checkExit(status);
    } else {
      super.checkExit(status);
    }
  }

  @Override
  public void checkExec(String cmd) {
    if (Objects.nonNull(delegate)) {
      delegate.checkExec(cmd);
    } else {
      super.checkExec(cmd);
    }
  }

  @Override
  public void checkLink(String lib) {
    if (Objects.nonNull(delegate)) {
      delegate.checkLink(lib);
    } else {
      super.checkLink(lib);
    }
  }

  @Override
  public void checkRead(FileDescriptor fd) {
    if (Objects.nonNull(delegate)) {
      delegate.checkRead(fd);
    } else {
      super.checkRead(fd);
    }
  }

  @Override
  public void checkRead(String file) {
    if (Objects.nonNull(delegate)) {
      delegate.checkRead(file);
    } else {
      super.checkRead(file);
    }
  }

  @Override
  public void checkRead(String file, Object context) {
    if (Objects.nonNull(delegate)) {
      delegate.checkRead(file, context);
    } else {
      super.checkRead(file, context);
    }
  }

  @Override
  public void checkWrite(FileDescriptor fd) {
    if (Objects.nonNull(delegate)) {
      delegate.checkWrite(fd);
    } else {
      super.checkWrite(fd);
    }
  }

  @Override
  public void checkWrite(String file) {
    if (Objects.nonNull(delegate)) {
      delegate.checkWrite(file);
    } else {
      super.checkWrite(file);
    }
  }

  @Override
  public void checkDelete(String file) {
    if (Objects.nonNull(delegate)) {
      delegate.checkDelete(file);
    } else {
      super.checkDelete(file);
    }
  }

  @Override
  public void checkConnect(String host, int port) {
    if (Objects.nonNull(delegate)) {
      delegate.checkConnect(host, port);
    } else {
      super.checkConnect(host, port);
    }
  }

  @Override
  public void checkConnect(String host, int port, Object context) {
    if (Objects.nonNull(delegate)) {
      delegate.checkConnect(host, port, context);
    } else {
      super.checkConnect(host, port, context);
    }
  }

  @Override
  public void checkListen(int port) {
    if (Objects.nonNull(delegate)) {
      delegate.checkListen(port);
    } else {
      super.checkListen(port);
    }
  }

  @Override
  public void checkAccept(String host, int port) {
    if (Objects.nonNull(delegate)) {
      delegate.checkAccept(host, port);
    } else {
      super.checkAccept(host, port);
    }
  }

  @Override
  public void checkMulticast(InetAddress maddr) {
    if (Objects.nonNull(delegate)) {
      delegate.checkMulticast(maddr);
    } else {
      super.checkMulticast(maddr);
    }
  }

  @Override
  @Deprecated
  public void checkMulticast(InetAddress maddr, byte ttl) {
    if (Objects.nonNull(delegate)) {
      delegate.checkMulticast(maddr, ttl);
    } else {
      super.checkMulticast(maddr, ttl);
    }
  }

  @Override
  public void checkPropertiesAccess() {
    if (Objects.nonNull(delegate)) {
      delegate.checkPropertiesAccess();
    } else {
      super.checkPropertiesAccess();
    }
  }

  @Override
  public void checkPropertyAccess(String key) {
    if (Objects.nonNull(delegate)) {
      delegate.checkPropertyAccess(key);
    } else {
      super.checkPropertyAccess(key);
    }
  }

  @Override
  @Deprecated
  public boolean checkTopLevelWindow(Object window) {
    return Objects.nonNull(delegate) ? delegate.checkTopLevelWindow(window) : super.checkTopLevelWindow(window);
  }

  @Override
  public void checkPrintJobAccess() {
    if (Objects.nonNull(delegate)) {
      delegate.checkPrintJobAccess();
    } else {
      super.checkPrintJobAccess();
    }
  }

  @Override
  @Deprecated
  public void checkSystemClipboardAccess() {
    if (Objects.nonNull(delegate)) {
      delegate.checkSystemClipboardAccess();
    } else {
      super.checkSystemClipboardAccess();
    }
  }

  @Override
  @Deprecated
  public void checkAwtEventQueueAccess() {
    if (Objects.nonNull(delegate)) {
      delegate.checkAwtEventQueueAccess();
    } else {
      super.checkAwtEventQueueAccess();
    }
  }

  @Override
  public void checkPackageAccess(String pkg) {
    if (Objects.nonNull(delegate)) {
      delegate.checkPackageAccess(pkg);
    } else {
      super.checkPackageAccess(pkg);
    }
  }

  @Override
  public void checkPackageDefinition(String pkg) {
    if (Objects.nonNull(delegate)) {
      delegate.checkPackageDefinition(pkg);
    } else {
      super.checkPackageDefinition(pkg);
    }
  }

  @Override
  public void checkSetFactory() {
    if (Objects.nonNull(delegate)) {
      delegate.checkSetFactory();
    } else {
      super.checkSetFactory();
    }
  }

  @Override
  @Deprecated
  @CallerSensitive
  public void checkMemberAccess(Class<?> clazz, int which) {
    if (Objects.nonNull(delegate)) {
      delegate.checkMemberAccess(clazz, which);
    } else {
      super.checkMemberAccess(clazz, which);
    }
  }

  @Override
  public void checkSecurityAccess(String target) {
    if (Objects.nonNull(delegate)) {
      delegate.checkSecurityAccess(target);
    } else {
      super.checkSecurityAccess(target);
    }
  }

  @Override
  public ThreadGroup getThreadGroup() {
    return Objects.nonNull(delegate) ? delegate.getThreadGroup() : super.getThreadGroup();
  }
}
