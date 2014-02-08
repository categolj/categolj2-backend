package am.ik.categolj2.infra.search;

import org.hibernate.search.bridge.StringBridge;
import org.joda.time.DateTime;

public class DateTimeBridge implements StringBridge {

    @Override
    public String objectToString(Object object) {
        DateTime datetime = (DateTime) object;
        String date = datetime.toString("yyyy-MM-dd");
        return date;
    }

}