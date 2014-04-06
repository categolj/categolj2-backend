package am.ik.categolj2.infra.logging;

import org.slf4j.MDC;

import java.lang.management.ManagementFactory;

public class PidSetter {

    public PidSetter() {
        if (System.getProperty("PID") == null) {
            String pid = getPid();
            System.setProperty("PID", pid);
            MDC.put("PID", pid);
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
