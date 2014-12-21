package am.ik.categolj2.infra.log4jdbc;


import net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator;
import net.sf.log4jdbc.sql.Spy;

public class GetMaxRowsIgnoringSlf4jSpyLogDelegator extends Slf4jSpyLogDelegator {
    @Override
    public void exceptionOccured(Spy spy, String methodCall, Exception e, String sql, long execTime) {
        if (methodCall != null && methodCall.startsWith("getMaxRows")) {
            // ignore
            return;
        }
        super.exceptionOccured(spy, methodCall, e, sql, execTime);
    }
}
