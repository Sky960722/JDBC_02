package com.atguigu5.dbutils;

import com.atguigu4.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import java.sql.Connection;

public class QueryRunnerTest {

    @Test
    public void testInsert() throws Exception{
        QueryRunner runner = new QueryRunner();

        Connection conn = JDBCUtils.getConnection3();
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int count = runner.update(conn, sql, "蔡徐坤", "caixukun@126.com", "1997-09-08");
        System.out.println("添加了"+count+"条记录");

        JDBCUtils.closeResource(conn,null);
        System.out.println("hello");
        System.out.println("asfdasdsry");
        System.out.println("hello,git4dfad-master test");

    }
}
