package com.atguigu4.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class JDBCUtils {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");

    public static Connection getConnection1() throws Exception {

        Connection conn = cpds.getConnection();
        return conn;

    }

    private static DataSource sources;

    static {

        try {
            Properties pros = new Properties();
            FileInputStream fis = new FileInputStream(new File("src/dpcp.properties"));
            pros.load(fis);
            sources = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection2() throws Exception{
        Connection conn = sources.getConnection();
        return conn;
    }

    private static DataSource source1;
    static {
        try {
            Properties pros =new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection3() throws SQLException {
        Connection conn = source1.getConnection();
        return conn;

    }

    public static void closeResource(Connection conn, PreparedStatement ps)  {
        try {
            if(ps != null){
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection conn, Statement ps, ResultSet rs){
        try {
            if (ps != null)
                ps.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if(conn!=null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            if(rs != null){
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
