package com.aswl.as.user.filter;

public class RoleContextHolder {


    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setRole(String regionCode) {
        CONTEXT.set(regionCode);
    }

    public static String getRole() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
