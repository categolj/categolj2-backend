package am.ik.categolj2.core.web.pid;

import org.terasoluna.gfw.web.logging.mdc.AbstractMDCPutFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PidMDCPutFilter extends AbstractMDCPutFilter {

    @Override
    protected String getMDCKey(HttpServletRequest request, HttpServletResponse response) {
        return "X-PID";
    }

    @Override
    protected String getMDCValue(HttpServletRequest request, HttpServletResponse response) {
        return System.getProperty("X-PID");
    }
}
