package utils;

import javax.sql.DataSource;

import models.Book;
import org.postgresql.ds.PGSimpleDataSource;

public class DB_Connection {
    private static DataSource dataSource = null;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerName("localhost");
            ds.setDatabaseName("perpus_java");
            ds.setUser("postgres");
            ds.setPassword("admin123");
            ds.setPortNumbers(new int[]{5432});
            dataSource = ds;
        }
        return dataSource;
    }
}
