package main.models.entities;

import main.models.entities.Admin;
import main.models.entities.User;

public class Session {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Admin> adminThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
        adminThreadLocal.remove();
    }

    public static void setAdmin(Admin admin) {
        adminThreadLocal.set(admin);
        userThreadLocal.remove();
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static Admin getAdmin() {
        return adminThreadLocal.get();
    }

    public static void clearSession() {
        userThreadLocal.remove();
        adminThreadLocal.remove();
    }
}