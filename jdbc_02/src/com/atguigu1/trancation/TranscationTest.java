package com.atguigu1.trancation;

import com.atguigu1.util.JDBCUtils;
import com.sun.tracing.dtrace.ArgsAttributes;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

public class TranscationTest {

    @Test
    public void testUpdate() {
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");

        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");
        System.out.println("转账成功");

    }

    //通用的增删改查操作
    public int update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();

            //2.预编译sql语句
            ps = conn.prepareStatement(sql);

            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //返货影响的行数
            int count = ps.executeUpdate();
            return count;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
        //6
        return 0;
    }


    @Test
    public void testUpdateWithTx() {

        Connection conn = null;
        try {
            //1.获取连接
            conn = JDBCUtils.getConnection();
            System.out.println(conn.getAutoCommit());

            //2.设置自动提交为false
            conn.setAutoCommit(false);

            //3.修改数据
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql1, "AA");

            //模拟网络异常
            System.out.println((10 / 0));

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql1, "AA");
            System.out.println("转账成功");

            //4.提交事务
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            //5.释放资源
            JDBCUtils.closeResource(conn, null);
        }


    }


    //通用的增删改查操作----version 2.0 （考虑上事务）
    public int update(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句
            ps = conn.prepareStatement(sql);

            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //3.返回执行的结果
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //4.关闭资源
            JDBCUtils.closeResource(null, ps);
        }
        return 0;
    }

    @Test
    public void testTransactionSelect() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = JDBCUtils.getConnection();

        //System.out.println(conn.getTransactionIsolation());
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        conn.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "update user_table set balance = ? where user = ? ";

        conn.setAutoCommit(false);
        update(conn,sql, 5000,"CC");

        Thread.sleep(150000);
        System.out.println("修改结束");
    }

    /**
     * 通用的查询操作，用于返回数据表中的一条记录
     *
     * @param conn
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T t = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field fi = t.getClass().getDeclaredField(columnLabel);
                    fi.setAccessible(true);
                    fi.set(t, columnValue);
                }
                return t;
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }
}
