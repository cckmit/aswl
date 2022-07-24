package com.aswl.as.user.filter;

public class RegionCodeContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setRegionCode(String regionCode) {
        CONTEXT.set(regionCode);
    }

    public static String getRegionCode() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
