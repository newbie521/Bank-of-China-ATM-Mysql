package com.example.atm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Mysql数据库的连接辅助类
 */
public class LocalHelper {
//    使用原生连接数据库  ，以后建议使用MyBatis plus
    final static String cls="com.mysql.jdbc.Driver";
    final static String URL="jdbc:mysql://101.132.133.144:3306/atm-master?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    final static String USER="root";
    final static String PWD="123456";

    public static Connection connection;//连接集
    public static  Statement statement;//命令集
    public static PreparedStatement preparedStatement;//预编译命令集
    public  static ResultSet resultSet;//结果集
    //获取连接
    public static  void getconnetction(){
     try {
         Class.forName(cls);
         connection=DriverManager.getConnection(URL,USER,PWD);

     } catch (ClassCastException e){
         e.printStackTrace();
     } catch (Exception e)
     {
         e.printStackTrace();
     }
    }

    public static void closeAll()
    {
        try {
            if(resultSet!=null)
            {
                resultSet.close();
                resultSet=null;
            }
            if(statement!=null)
            {
                statement.close();
                statement =null;
            }
            if(preparedStatement!=null)
            {
                preparedStatement .close();
                preparedStatement =null;
            }
            if(connection!=null)
            {
                connection.close();
                connection=null;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
