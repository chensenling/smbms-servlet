package com.chensenling.dao.provider;

import com.chensenling.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ProviderDao {
    // 根据供应商的编码或名称查询列表
    public List<Provider> queryProviderByCodeAndName(Connection connection,String queryProCode,String queryProName,int currentPageNo,int pageSize);
    // 根据供应商的编码或名称查询列表总记录数
    public int getTotalNum(Connection connection,String queryProCode,String queryProName);

    //增加供应商
    public int addPro(Connection connection,Provider provider);
    // 按Code在数据库里面查
    public Provider queryProviderByProCode(Connection connection, String proCode) throws SQLException;

    // 根据pid 用户编号查询供应商
    public Provider getProviderByPid(Connection connection,int pid) throws SQLException;

    // 根据表单数据更新供应商
    public int updateProviderByForm(Connection connection,Provider provider) throws SQLException;

    //删除供应商
    public int delProviderByPid(Connection connection,int pid) throws SQLException;

    public List<Provider> getProviderList(Connection connection, String proName, String proCode)throws Exception;


}
