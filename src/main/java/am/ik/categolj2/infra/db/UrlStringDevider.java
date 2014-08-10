package am.ik.categolj2.infra.db;

import lombok.Getter;
import lombok.ToString;

import java.net.URI;
import java.net.URISyntaxException;

@Getter
@ToString
public class UrlStringDevider {
    private final String url;
    private final String username;
    private final String password;


    public UrlStringDevider(String databaseUrl) throws URISyntaxException {
        this(databaseUrl, "");
    }

    public UrlStringDevider(String databaseUrl, String appendUrl) throws URISyntaxException {
        URI dbUri = new URI(databaseUrl);
        url = "jdbc:" + dbUri.getScheme() + "://" + dbUri.getHost()
                + (dbUri.getPort() > 0 ? ":" + dbUri.getPort() : "")
                + dbUri.getPath()
                + (appendUrl.startsWith("?") ? appendUrl + "&" + dbUri.getQuery() : "?" + dbUri.getQuery() + (appendUrl.isEmpty() ? "" : "&" + appendUrl));
        username = dbUri.getUserInfo().split(":")[0];
        password = dbUri.getUserInfo().split(":")[1];
    }
}
