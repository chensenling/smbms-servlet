package com.chensenling.service.provider;

import com.chensenling.dao.BaseDao;
import com.chensenling.dao.provider.ProviderDao;
import com.chensenling.dao.provider.ProviderDaoImpl;
import com.chensenling.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService {

    private ProviderDao providerDao;
    public ProviderServiceImpl(){
        this.providerDao=new ProviderDaoImpl() ;
    }

    public List<Provider> getProviderList(String proName,String proCode) {
        // TODO Auto-generated method stub
        Connection connection = null;
        List<Provider> providerList = null;
        System.out.println("query proName ---- > " + proName);
        System.out.println("query proCode ---- > " + proCode);
        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection, proName,proCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return providerList;
    }


    // 根据供应商的编码或名称查询列表
    public List<Provider> queryProviderByCodeAndName(String queryProCode, String queryProName,int currentPageNo,int pageSize){
        Connection connection=null;
        List<Provider> proList=null;

        connection= BaseDao.getConnection();
        proList=providerDao.queryProviderByCodeAndName(connection,queryProCode,queryProName,currentPageNo,pageSize);

        BaseDao.closeResource(connection,null,null);
        return proList;

    }
    // 获取到总记录数
    public int getTotalCount(String queryProCode, String queryProName){
        Connection connection=null;
        int totalCount=0;

        connection=BaseDao.getConnection();
        totalCount=providerDao.getTotalNum(connection,queryProCode,queryProName);
        BaseDao.closeResource(connection,null,null);
        return  totalCount;
    }

    //增加供应商
    public boolean addPro(Provider provider){
        Connection connection=null;
        connection=BaseDao.getConnection();
        boolean flag=false;
        int rows=0;

        if (provider==null){
            return flag;
        }
        rows =providerDao.addPro(connection,provider);
        if(rows!=0){
            flag=true;
        }
        BaseDao.closeResource(connection,null,null);
        return flag;

    }
    // 按Code在数据库里面查
    public Provider queryProviderByProCode(String proCode) throws SQLException {

        Connection connection=null;
      Provider pro=null;
        connection=BaseDao.getConnection();
        pro=providerDao.queryProviderByProCode(connection,proCode);

        BaseDao.closeResource(connection,null,null);

        return  pro;
    }
    // 根据pid 用户编号查询供应商
    public Provider getProviderByPid(int pid) throws SQLException {
        Connection connection=null;
      Provider provider=null;
        connection=BaseDao.getConnection();

        provider =providerDao.getProviderByPid(connection,pid);
        //释放connection
        BaseDao.closeResource(connection,null,null);
        return provider;
    }

    // 根据表单数据更新供应商
    public boolean updateProviderByForm(Provider provider) throws SQLException {
        Connection connection =null;
        int rows=0;
        boolean flag =true;
        connection=BaseDao.getConnection();
        rows =providerDao.updateProviderByForm(connection,provider);

        BaseDao.closeResource(connection,null,null);
        if(rows== 0){
            flag =false;
        }


        return flag;
    }
    //删除供应商
    public boolean delProviderByPid(int pid) throws SQLException {
        Connection connection=null;
        boolean flag=true;
        int rows=0;

        connection=BaseDao.getConnection();

        rows =providerDao.delProviderByPid(connection,pid);

        if (rows==0){
            flag=false;
        }
        return flag;
    }
    /*  @Test*/
   /* public void test(){
        List<Provider> list=new ProviderServiceImpl().queryProviderByCodeAndName(null,null);
        String s =list.toArray().toString();
        System.out.println(s);
    }*/

}
