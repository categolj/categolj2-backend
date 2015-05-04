package am.ik.categolj2.infra.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jdbc.pool.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class ConnectionValidator implements Validator {

    @Override
    public boolean validate(Connection connection, int validateAction) {
        try {
            connection.setReadOnly(true);
        } catch (SQLException e) {
            if (log.isWarnEnabled()) {
                log.warn("setReadOnly(true) failed! connection=" + connection + " action=" + validateAction, e);
            }
        }
        try (Statement statement = connection.createStatement()) {
            log.trace("validate query connection={} action={}", connection, validateAction);
            statement.setQueryTimeout(5);
            statement.execute("SELECT 1");
        } catch (SQLException e) {
            if (log.isWarnEnabled()) {
                log.warn("validating query failed! connection=" + connection + " action=" + validateAction, e);
            }
            return false;
        }
        return true;
    }
}
