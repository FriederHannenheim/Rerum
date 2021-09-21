package fhannenheim.rerum.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class DataUtil {
    public static class CustomHashMap<K, V> extends HashMap<K, V> {
        @Override
        public V put(K key, V value) {
            super.put(key, value);
            return value;
        }
    }
}
