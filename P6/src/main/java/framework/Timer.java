package main.java.framework;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Timer {

    private ThreadMXBean threadMXBean;
    private boolean supportsCpuTime;
    private long timeStart = -1;
    private long timeStop = -1;

    public Timer() {
        this.threadMXBean = ManagementFactory.getThreadMXBean();
        this.supportsCpuTime = false;
    }

    public void start() {
        if (timeStart == -1) {
            if (this.supportsCpuTime) {
                this.timeStart = threadMXBean.getCurrentThreadCpuTime();
            } else {
                this.timeStart = System.nanoTime();
            }
        }
    }

    public void stop() {
        if (timeStop == -1) {
            if (this.supportsCpuTime) {
                this.timeStop = threadMXBean.getCurrentThreadCpuTime();
            } else {
                this.timeStop = System.nanoTime();
            }
        }
    }

    public long getDuration() {
        if (this.timeStart == -1 || this.timeStop == -1) {
            return -1;
        } else {
            return this.timeStop - this.timeStart;
        }
    }
}
