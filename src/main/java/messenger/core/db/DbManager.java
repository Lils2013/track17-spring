package messenger.core.db;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 */
@Component
public class DbManager {
    private String url;
    private String login;
    private String pass;

    private Connection connection;

    public DbManager() {
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @PostConstruct
    public void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + url);
    }

    public Connection getConnection() {
        return connection;
    }
}
