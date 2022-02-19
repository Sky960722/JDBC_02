package com.atguigu4.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DPCPTest {

    //方式一：不推荐
    @Test
    public void testGetConnection() throws SQLException {
        BasicDataSource source = new BasicDataSource();

        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("000000");

        source.setInitialSize(10);
        source.setMaxActive(10);


        Connection connection = source.getConnection();
        System.out.println(connection);

    }

    @Test
    public void testGetCOnnection1() throws Exception{
        Properties pros = new Properties();
        //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dpcp.properties");
        FileInputStream fis = new FileInputStream(new File("src/dpcp.properties"));
        pros.load(fis);

        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection connection = source.getConnection();
        System.out.println(connection);
    }
}
