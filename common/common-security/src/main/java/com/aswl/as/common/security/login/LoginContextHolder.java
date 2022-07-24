package com.aswl.as.common.security.login;

/**
 * ThreadLocal保存租户code
 *
 * @author aswl.com
 * @date 2019/5/28 20:54
 */
public class LoginContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setIsAdmin(String isAdmin) {
        CONTEXT.set(isAdmin);
    }

    public static String getIsAdmin() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
