package com.ideas.uno.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author TAPANM
 */
public class DeckUtility {

    public static <T, V> void addToMapOfList(Map<T, List<V>> map, T key, V value) {
        if (map.get(key) == null) {
            List<V> list = new ArrayList<V>();
            map.put(key, list);
        }
        map.get(key).add(value);
    }

    public static <T, V> void removeToMapOfList(Map<T, List<V>> map, T key, V value) {
        if (map.get(key) != null && map.get(key).size() > 0) {
            map.get(key).remove(value);
        }
    }

    public static <V> boolean isEmpty(List<V> list) {
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }
}
