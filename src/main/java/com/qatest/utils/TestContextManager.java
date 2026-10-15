/**
 * 
 */
package com.qatest.utils;

/**
 * @author Divyang
 *
 */
import java.util.HashMap;
import java.util.Map;

public class TestContextManager {
    private static final ThreadLocal<Map<String, Object>> threadStore = 
        ThreadLocal.withInitial(HashMap::new);

    public static void setAttribute(String key, Object value) {
        threadStore.get().put(key, value);
    }

    public static Object getAttribute(String key) {
        return threadStore.get().get(key);
    }

    public static void clear() {
        threadStore.remove();
    }
}