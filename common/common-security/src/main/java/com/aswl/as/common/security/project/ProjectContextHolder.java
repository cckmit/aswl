package com.aswl.as.common.security.project;

/**
 * ThreadLocal保存租户code
 *
 * @author aswl.com
 * @date 2019/5/28 20:54
 */
public class ProjectContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setProjectId(String projectId) {
        CONTEXT.set(projectId);
    }

    public static String getProjectId() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
