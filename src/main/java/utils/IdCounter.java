package utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdCounter {
    static private final AtomicLong atomicLong=new AtomicLong();

    public static long getAndIncrement(){
        synchronized (atomicLong) {
            return atomicLong.getAndIncrement();
        }
    }
}
