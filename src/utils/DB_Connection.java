package utils;

import javax.sql.DataSource;

import config.Constant;
import models.Book;
import org.postgresql.ds.PGSimpleDataSource;

public class DB_Connection {
    private static DataSource dataSource = null;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerName(Constant.DB_HOSTNAME);
            ds.setDatabaseName(Constant.DB_NAME);
            ds.setUser(Constant.DB_USERNAME);
            ds.setPassword(Constant.DB_PASSWORD);
            ds.setPortNumbers(new int[]{Constant.DB_PORT});
            dataSource = ds;
        }
        return dataSource;
    }
}
