package com.chensenling.dao.provider;

import com.chensenling.dao.BaseDao;
import com.chensenling.pojo.Provider;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl  implements  ProviderDao{
    // 根据供应商的编码或名称查询列表
    public List<Provider> queryProviderByCodeAndName(Connection connection, String queryProCode, String queryProName,int currentPageNo,int pageSize){

        PreparedStatement pre=null;
        ResultSet resultSet=null;
        Provider provider=null;
        List<Provider> proList=new ArrayList<Provider>();
        System.out.println(queryProCode+" "+queryProName);
        if(connection!=null){
            // 动态sql
            StringBuffer sql=new StringBuffer();
            sql.append("select * from smbms_provider p ");

            List<Object> list=new ArrayList<Object>();

            if((queryProCode!=null&&!queryProCode.equals(""))&&(queryProName!=null&&!queryProName.equals(""))){
                System.out.println("进入双条件查询");
                sql.append("where  p.proCode=? and p.proName=?");
                System.out.println(sql.toString());
                list.add(queryProCode);
                list.add(queryProName);
                System.out.println(list.get(0)+" "+list.get(1));
            }
            if((queryProCode!=null&&!queryProCode.equals("")) &&(queryProName==null||queryProName.equals("") )){
                System.out.println("进入code查询");

                sql.append("where  p.proCode=?");
                System.out.println(sql.toString());
                list.add(queryProCode);
                System.out.println(list.get(0)+" ");
            }
            if((queryProName!=null&&!queryProName.equals(""))&&(queryProCode==null||queryProCode.equals("")) ){
                System.out.println("进入name查询");
                sql.append("where  p.proName=?");
                System.out.println(sql.toString());
                list.add(queryProName);
                System.out.println(list.get(0)+" ");
            }
            sql.append(" order by p.creationDate desc limit ?,?");


            int index=(currentPageNo-1)*pageSize;

            Object[] params=null;


                list.add(index);
                list.add(pageSize);
                params=list.toArray();


            try {
               resultSet= BaseDao.execute(connection,sql.toString(),params,resultSet,pre);
               while(resultSet.next()){
                   provider =new Provider();
                   provider.setId(resultSet.getInt("id"));
                   provider.setProCode(resultSet.getString("proCode"));
                   provider.setProName(resultSet.getString("proName"));
                   provider.setProDesc(resultSet.getString("proDesc"));
                   provider.setProContact(resultSet.getString("proContact"));
                   provider.setProPhone(resultSet.getString("proPhone"));
                   provider.setProAddress(resultSet.getString("proAddress"));
                   provider.setProFax(resultSet.getString("proFax"));
                   provider.setCreatedBy(resultSet.getInt("createdBy"));
                   provider.setCreationDate(resultSet.getDate("creationDate"));
                   provider.setModifyBy(resultSet.getInt("modifyBy"));
                   provider.setModifyDate(resultSet.getDate("modifyDate"));

                   proList.add(provider);
               }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                BaseDao.closeResource(null,resultSet,pre);
            }

        }
        return proList;
    }
    // 总记录数
    public int getTotalNum(Connection connection,String queryProCode,String queryProName){
        int totalCount=0;
        PreparedStatement pre=null;
        ResultSet resultSet=null;


        System.out.println(queryProCode+" "+queryProName);
        if(connection!=null){
            // 动态sql
            StringBuffer sql=new StringBuffer();
            sql.append("select count(1) as count from smbms_provider p ");

            List<String> list=new ArrayList<String>();

            if((queryProCode!=null&&!queryProCode.equals(""))&&(queryProName!=null&&!queryProName.equals(""))){
                System.out.println("进入双条件查询");
                sql.append("where  p.proCode=? and p.proName=?");
                System.out.println(sql.toString());
                list.add(queryProCode);
                list.add(queryProName);
                System.out.println(list.get(0)+" "+list.get(1));
            }
            if((queryProCode!=null&&!queryProCode.equals("")) &&(queryProName==null||queryProName.equals("") )){
                System.out.println("进入code查询");

                sql.append("where  p.proCode=?");
                System.out.println(sql.toString());
                list.add(queryProCode);
                System.out.println(list.get(0)+" ");
            }
            if((queryProName!=null&&!queryProName.equals(""))&&(queryProCode==null||queryProCode.equals("")) ){
                System.out.println("进入name查询");
                sql.append("where  p.proName=?");
                System.out.println(sql.toString());
                list.add(queryProName);
                System.out.println(list.get(0)+" ");
            }


            Object[] params=null;
            if(list!=null){

                params=list.toArray();
            }
            try {
                resultSet= BaseDao.execute(connection,sql.toString(),params,resultSet,pre);
                while(resultSet.next()){
                   totalCount=resultSet.getInt("count");

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                BaseDao.closeResource(null,resultSet,pre);
            }

        }
        return totalCount;

    }

    //增加供应商
    public int addPro(Connection connection,Provider provider){
        PreparedStatement pre=null;
        int rows=0;
        if(connection!=null) {

            String sql = "insert into  smbms_provider  values(null,?,?,?,?,?,?,?,?,?,?,?)";


            ArrayList<Object> list = new ArrayList<Object>();
              list.add(provider.getProCode());
              list.add(provider.getProName());
            list.add(provider.getProDesc());
            list.add(provider.getProContact());
            list.add(provider.getProPhone());
            list.add(provider.getProAddress());
            list.add(provider.getProFax());
            list.add(provider.getCreatedBy());
            list.add(provider.getCreationDate());
            list.add(provider.getModifyDate());
            list.add(provider.getModifyBy());



            Object[] params = list.toArray();
            System.out.println(params.toString());
            try {
                rows = BaseDao.execute(connection, sql, params, pre);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                BaseDao.closeResource(null, null, pre);
            }


        }
        System.out.println(rows);
        return rows;
    }

    // 按Code在数据库里面查
    public Provider queryProviderByProCode(Connection connection, String proCode) throws SQLException
    {
        PreparedStatement pre =null;
        ResultSet resultSet=null;
        int rows=0;
        Provider provider=null;

        String sql="select p.proCode from smbms_provider p where p.proCode=?";
        Object[] params={proCode};
        resultSet = BaseDao.execute(connection,sql,params,resultSet,pre);
        while(resultSet.next()){
            provider=new Provider();
            try {
                provider.setProCode(resultSet.getString("proCode"));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        BaseDao.closeResource(null,resultSet,pre);
        return provider;
    }
    // 根据pid 用户编号查询供应商
    public Provider getProviderByPid(Connection connection, int pid) throws SQLException {
        PreparedStatement pre=null;
        ResultSet resultSet =null;
        Provider provider=null;
        if(connection!=null){

            String sql="select * from smbms_provider  p where p.id=?";

            Object[] params={pid};

            resultSet =BaseDao.execute(connection,sql,params,resultSet,pre);
            while (resultSet.next()){
                provider=new Provider();
                provider.setId(resultSet.getInt("id"));
                provider.setProCode(resultSet.getString("proCode"));
                provider.setProName(resultSet.getString("proName"));
                provider.setProDesc(resultSet.getString("proDesc"));
                provider.setProContact(resultSet.getString("proContact"));
                provider.setProPhone(resultSet.getString("proPhone"));
                provider.setProAddress(resultSet.getString("proAddress"));
                provider.setProFax(resultSet.getString("proFax"));
                provider.setCreatedBy(resultSet.getInt("createdBy"));
                provider.setCreationDate(resultSet.getDate("creationDate"));
                provider.setModifyDate(resultSet.getDate("modifyDate"));
                provider.setModifyBy(resultSet.getInt("modifyBy"));


            }
            System.out.println(resultSet);
            System.out.println(sql);

            // 释放资源
            BaseDao.closeResource(null,resultSet,pre);



        }
        return provider;

    }
    // 根据表单数据更新供应商
    public int updateProviderByForm(Connection connection, Provider provider) throws SQLException {
        PreparedStatement pre =null;
        int rows =0;
        if(connection!=null){

            String sql ="update smbms_provider p set p.proCode=?,p.proName=?,p.proDesc=?,p.proContact=?,p.proPhone=?,p.proAddress=?,p.proFax=?,p.createdBy=?,p.creationDate=?,p.modifyDate=?,p.modifyBy=? where p.id=?";
            ArrayList<Object> list=new ArrayList<Object>();


            list.add(provider.getProCode());
            list.add(provider.getProName());
            list.add(provider.getProDesc());
            list.add(provider.getProContact());
            list.add(provider.getProPhone());
            list.add(provider.getProAddress());
            list.add(provider.getProFax());
            list.add(provider.getCreatedBy());
            list.add(provider.getCreationDate());
            list.add(provider.getModifyDate());
            list.add(provider.getModifyBy());
            list.add(provider.getId());

            Object[] params =list.toArray();


//            System.out.println(params[params.length-1]);//
            rows= BaseDao.execute(connection,sql,params,pre);

            BaseDao.closeResource(null,null,pre);

        }
        return rows;
    }
    //删除供应商
    public int delProviderByPid(Connection connection, int pid) throws SQLException {

        PreparedStatement pre=null;
        int rows=0;

        if(connection!=null){
            String sql ="delete from smbms_provider  where smbms_provider.id= ?";
            Object[] params={pid};
            rows = BaseDao.execute(connection, sql, params, pre);
        }
        BaseDao.closeResource(null,null,pre);
        return  rows;
    }

    public List<Provider> getProviderList(Connection connection, String proName,String proCode)
            throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Provider> providerList = new ArrayList<Provider>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select * from smbms_provider where 1=1 ");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(proName)){
                sql.append(" and proName like ?");
                list.add("%"+proName+"%");
            }
            if(!StringUtils.isNullOrEmpty(proCode)){
                sql.append(" and proCode like ?");
                list.add("%"+proCode+"%");
            }
            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, sql.toString(),params,rs,pstm);
            while(rs.next()){
                Provider _provider = new Provider();
                _provider.setId(rs.getInt("id"));
                _provider.setProCode(rs.getString("proCode"));
                _provider.setProName(rs.getString("proName"));
                _provider.setProDesc(rs.getString("proDesc"));
                _provider.setProContact(rs.getString("proContact"));
                _provider.setProPhone(rs.getString("proPhone"));
                _provider.setProAddress(rs.getString("proAddress"));
                _provider.setProFax(rs.getString("proFax"));
                _provider.setCreationDate(rs.getTimestamp("creationDate"));
                providerList.add(_provider);
            }
            BaseDao.closeResource(null, rs, pstm);
        }
        return providerList;
    }

}
