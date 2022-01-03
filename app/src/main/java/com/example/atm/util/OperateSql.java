package com.example.atm.util;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OperateSql extends LocalHelper{



    public static int getBalance(String account)
    { int balance = -1;
        try
        {
            getconnetction();
            String sql="select balance from acc where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                balance=resultSet.getInt("balance");
                android.util.Log.d("查到了xx",Integer.toString(balance));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return balance;

    }

    public static void updateBalance(String account,int balance)
    {
        try{
            getconnetction();
            String sql="update acc set balance=? where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,balance);
            preparedStatement.setString(2,account);
            preparedStatement.executeUpdate();//一定要有这一条语句

            android.util.Log.d("更新了余额: ",Integer.toString(balance));

        }catch (Exception e)
        {e.printStackTrace();}
        finally {
            closeAll();
        }
    }

    public static int getAvi_Balance(String account)
    { int avi_balance=-1;
        try
        {
            getconnetction();
            String sql="select avi_balance from acc where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                avi_balance=resultSet.getInt("avi_balance");
                android.util.Log.d("查到了",Integer.toString(avi_balance));
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

        return avi_balance;
    }

    public static void updateAvi_Balance(String account,int avi_balance)
    {
        try{
            getconnetction();
            String sql="update acc set avi_balance=? where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,avi_balance);
            preparedStatement.setString(2,account);
            preparedStatement.executeUpdate();//一定要有这一条语句

            android.util.Log.d("更新了每日限额: ",Integer.toString(avi_balance));

        }catch (Exception e)
        {e.printStackTrace();}
        finally {
            closeAll();
        }
    }

    public static String getPassword(String account)
    { String password=null;
        try
        {
            getconnetction();
            String sql="select password from acc where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                password=resultSet.getString("password");
                android.util.Log.d("查到了",password);
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

        return password;
    }

    public static void updatePassword(String account, String password)
    {
        try
        {
            getconnetction();
            String sql="update acc set password=? where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,password);
            preparedStatement.setString(2,account);
            preparedStatement.executeUpdate();
            android.util.Log.d("更新了密码: ",password);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

    }

//    插入账号
    public static void insert_account(String account,String password,int balance,int  avi_balance)
    {
        try{
            getconnetction();
            String sql="insert into acc(account,password,balance,avi_balance) values(?,?,?,?,?)";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString (1,account);
            preparedStatement.setString (2,password);
            preparedStatement.setInt(3,balance);
            preparedStatement.setInt(4,avi_balance);
            preparedStatement.execute();
        }catch (Exception e)
        {e.printStackTrace();}
        finally {
            closeAll();
        }
    }

    @SuppressLint("LongLogTag")
    public static HashMap<Integer, List<String>> getRecord(String account)
    {
        List<String> record = new ArrayList<>();
        HashMap<Integer, List<String>> map = new HashMap<>();
        int i =0;
        try
        {
            getconnetction();
            String sql="select * from information where account=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            // 执行sql查询语句并返回结果集
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next())
            {
                String date =resultSet.getString("date");
                String exchange =resultSet.getString("exchange");
                String type =resultSet.getString("type");

                record.add(date);
                record.add(exchange);
                record.add(type);

                android.util.Log.d("record",record.toString());

                map.put(i++, new LinkedList<String>(){{
                    add(date);
                    add(exchange);
                    add(type);
                }});
            }

            android.util.Log.d("#######################: ",map.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

        return map;
    }

    public static void updateRecord(List<String> record)
    {
        try
        {
            getconnetction();
            String sql="insert into information(account,date,exchange,type) values(?,?,?,?)";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,record.get(0));
            preparedStatement.setString(2,record.get(1));
            preparedStatement.setString(3,record.get(2));
            preparedStatement.setString(4,record.get(3));
            preparedStatement.execute();
            android.util.Log.d("添加了记录: ",record.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

    }

    public static String getLastDate(String account)
    {
        String date = null;
        try
        {
            getconnetction();
            String sql="select date from information where account=? ORDER By id Desc limit 1";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,account);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {

                date =resultSet.getString("date");

                android.util.Log.d("查到了",date);
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

        return date;
    }


}
