package controller.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

class IdMapped<T> {
    private final Map<Integer, T> mp = new HashMap<>();
    private final Random random = new Random();
    private int genRandom() {
        int ret = random.nextInt(mp.size() * 10+10);
        while(mp.containsKey(ret))
            ret = random.nextInt(mp.size() * 10+10);
        return ret;
    }
    public synchronized void foreach(Consumer<T> func) {
        mp.values().forEach(func);
    }
    public synchronized int addObj(T obj) {
        int id = genRandom();
        mp.put(id, obj);
        return id;
    }
    public synchronized void removeByKey(int id) {
        mp.remove(id);
    }
}
