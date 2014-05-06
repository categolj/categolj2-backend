package am.ik.categolj2.infra.logging;

import org.slf4j.MDC;

import java.lang.management.ManagementFactory;

public class PidSetter {

    public PidSetter() {
        if (System.getProperty("X-PID") == null) {
            String pid = getPid();
            System.setProperty("X-PID", pid);
            MDC.put("X-PID", pid);
        }
    }

    String getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            if (name != null) {
                return name.split("@")[0];
            }
        } catch (Throwable ex) {
            // Ignore
        }
        return "????";
    }
}
