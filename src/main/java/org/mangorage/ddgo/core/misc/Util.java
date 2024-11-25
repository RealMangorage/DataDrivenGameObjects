package org.mangorage.ddgo.core.misc;

import java.util.HashMap;
import java.util.Map;

public final class Util {
    // Deep merge two maps: merge only the entries that Map A has that Map B doesn't have.
    public static Map<String, Object> deepMerge(Map<String, Object> mapA, Map<String, Object> mapB) {
        Map<String, Object> resultMap = new HashMap<>(mapB);  // Start with Map B to retain its entries

        for (Map.Entry<String, Object> entryA : mapA.entrySet()) {
            String key = entryA.getKey();
            Object valueA = entryA.getValue();

            // Only add from A if key is not present in B
            if (!mapB.containsKey(key)) {
                resultMap.put(key, deepClone(valueA));
            } else {
                // If the key is present in both maps, we need to deep merge if both values are Maps
                Object valueB = mapB.get(key);
                if (valueA instanceof Map && valueB instanceof Map) {
                    resultMap.put(key, deepMerge((Map<String, Object>) valueA, (Map<String, Object>) valueB));
                }
            }
        }

        return resultMap;
    }

    // Helper function to deep clone a value: handles nested Maps
    private static Object deepClone(Object value) {
        if (value instanceof Map) {
            // If it's a Map, we need to deep merge (i.e., copy the map)
            Map<String, Object> clonedMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                clonedMap.put(entry.getKey(), deepClone(entry.getValue()));
            }
            return clonedMap;
        } else {
            // For non-Map values, just return the value as is
            return value;
        }
    }
}
