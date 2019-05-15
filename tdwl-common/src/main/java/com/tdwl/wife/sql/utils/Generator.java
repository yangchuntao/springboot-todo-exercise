package com.tdwl.wife.sql.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Yang Ct
 * @Description
 * @date
 **/
public class Generator {


    private Lock lock;
    private static final long TWEPOCH = 1546272000000L;
    private static final long GENERATOR_ID_BITS = 8L;
    private static final long MAX_GENERATOR_ID = 255L;
    private static final long SEQUENCE_BITS = 10L;
    private static final long GENERATOR_ID_SHIFT = 10L;
    private static final long TIMESTAMP_LEFT_SHIFT = 18L;
    private static final long SEQUENCE_MASK = 1023L;
    private long generatorId;
    private long sequence;
    private long lastTimestamp;

    public Generator() {
        this(255L);
    }

    public Generator(long generatorId) {
        this.lock = new ReentrantLock();
        this.sequence = 0L;
        this.lastTimestamp = -1L;
        if (generatorId <= 255L && generatorId >= 0L) {
            this.generatorId = generatorId;
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 255L));
        }
    }

    public synchronized long nextId() {
        this.lock.lock();

        long var5;
        try {
            long timestamp = this.timeGen();
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 1023L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            if (timestamp < this.lastTimestamp) {
                try {
                    throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
                } catch (Exception var10) {
                    var10.printStackTrace();
                }
            }

            this.lastTimestamp = timestamp;
            long nextId = timestamp - 1546272000000L << 18 | this.generatorId << 10 | this.sequence;
            var5 = nextId;
        } finally {
            this.lock.unlock();
        }

        return var5;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    private long timeGen() {
        //高并发的时候可以考虑使用原子性和纳秒
        return System.currentTimeMillis();
    }
}
