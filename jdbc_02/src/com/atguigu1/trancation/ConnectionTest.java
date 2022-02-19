package com.atguigu1.trancation;

import com.atguigu1.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    @Test
    public void testGetConnection() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
