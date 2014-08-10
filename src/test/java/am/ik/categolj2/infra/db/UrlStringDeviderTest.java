package am.ik.categolj2.infra.db;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UrlStringDeviderTest {

    @Test
    public void test() throws Exception {
        UrlStringDevider devider = new UrlStringDevider("mysql://USERNAME:PASSWD@eu-cdbr-west-01.cleardb.com/heroku_6e77b52634c2faa?reconnect=true");
        assertThat(devider.getUsername(), is("USERNAME"));
        assertThat(devider.getPassword(), is("PASSWD"));
        assertThat(devider.getUrl(), is("jdbc:mysql://eu-cdbr-west-01.cleardb.com/heroku_6e77b52634c2faa?reconnect=true"));
    }

    @Test
    public void test_appendUrl() throws Exception {
        UrlStringDevider devider = new UrlStringDevider("mysql://USERNAME:PASSWD@eu-cdbr-west-01.cleardb.com/heroku_6e77b52634c2faa?reconnect=true", "zeroDateTimeBehavior=convertToNull");
        assertThat(devider.getUsername(), is("USERNAME"));
        assertThat(devider.getPassword(), is("PASSWD"));
        assertThat(devider.getUrl(), is("jdbc:mysql://eu-cdbr-west-01.cleardb.com/heroku_6e77b52634c2faa?reconnect=true&zeroDateTimeBehavior=convertToNull"));
    }

    @Test
    public void test_appendUrl2() throws Exception {
        UrlStringDevider devider = new UrlStringDevider("mysql://USERNAME:PASSWD@eu-cdbr-west-01.cleardb.com/heroku_6e77b52634c2faa?reconnect=true", "?zeroDateTimeBehavior=convertToNull");
        assertThat(devider.getUsername(), is("USERNAME"));
        assertThat(devider.getPassword(), is("PASSWD"));
        assertThat(devider.getUrl(), is("jdbc:mysql://eu-cdbr-west-01.cleardb.com/heroku_6e77b52634c2faa?zeroDateTimeBehavior=convertToNull&reconnect=true"));
    }


}