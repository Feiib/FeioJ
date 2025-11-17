package com.fei.feiojcodesandbox.security;

import java.io.File;
import java.io.IOException;
import java.security.Permission;

public class DefaultSecurityManager extends SecurityManager {
    private final String allowedDir;
    private final String allowedJavaHome;
    private final String allowedAppClassesDir;

    public DefaultSecurityManager() {
        String dir = System.getProperty("sandbox.allowedDir");
        this.allowedDir = normalize(dir);
        this.allowedJavaHome = normalize(System.getProperty("java.home"));
        this.allowedAppClassesDir = normalize(System.getProperty("sandbox.appClassesDir"));
    }

    private String normalize(String path) {
        if (path == null) return null;
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException e) {
            return new File(path).getAbsolutePath();
        }
    }

    private boolean isPathAllowed(String path) {
        if (path == null) return false;
        String p = normalize(path);
        return (allowedDir != null && p.startsWith(allowedDir))
                || (allowedJavaHome != null && p.startsWith(allowedJavaHome))
                || (allowedAppClassesDir != null && p.startsWith(allowedAppClassesDir));
    }

    @Override
    public void checkPermission(Permission perm) {
        // 限制危险运行时权限
        if (perm instanceof java.lang.RuntimePermission) {
            String name = perm.getName();
            if ("setSecurityManager".equals(name)
                    || (name != null && name.startsWith("exitVM"))
                    || (name != null && name.startsWith("loadLibrary"))) {
                throw new SecurityException("Runtime permission denied: " + name);
            }
            // 放行JRE内部必要的classloader创建，阻止应用层恶意创建
        } else if (perm instanceof java.io.FilePermission) {
            String actions = perm.getActions(); // read, write, execute, delete
            String name = ((java.io.FilePermission) perm).getName();
            if (actions.contains("read")) {
                if (!isPathAllowed(name)) {
                    throw new SecurityException("File read not allowed: " + name);
                }
            }
            if (actions.contains("write") || actions.contains("delete") || actions.contains("execute")) {
                throw new SecurityException("File operation not allowed: " + actions + " -> " + name);
            }
        } else if (perm instanceof java.net.SocketPermission) {
            // 禁止所有网络访问
            throw new SecurityException("Network access is not allowed");
        } else if (perm instanceof java.util.PropertyPermission) {
            // 允许读取系统属性，禁止写入
            String actions = perm.getActions();
            if (actions.contains("write")) {
                throw new SecurityException("System property write not allowed");
            }
        }
        // 其它权限默认允许（如JRE内部所需的权限）
    }

    @Override
    public void checkRead(String file) {
        if (!isPathAllowed(file)) {
            throw new SecurityException("Read file not allowed: " + file);
        }
    }

    @Override
    public void checkWrite(String file) {
        throw new SecurityException("Write file not allowed: " + file);
    }

    @Override
    public void checkDelete(String file) {
        throw new SecurityException("Delete file not allowed: " + file);
    }

    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("Process execution not allowed: " + cmd);
    }

    @Override
    public void checkExit(int status) {
        throw new SecurityException("System exit not allowed");
    }

    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("Network connect not allowed");
    }

    @Override
    public void checkListen(int port) {
        throw new SecurityException("Network listen not allowed");
    }

    @Override
    public void checkAccept(String host, int port) {
        throw new SecurityException("Network accept not allowed");
    }
}
