package com.chensenling.service.provider;

import com.chensenling.pojo.Provider;

import java.sql.SQLException;
import java.util.List;

public interface ProviderService {
    // 根据供应商的编码或名称查询列表
    public List<Provider> queryProviderByCodeAndName( String queryProCode, String queryProName,int currentPageNo,int pageSize);

    // 获取到总记录数
    public int getTotalCount(String queryProCode, String queryProName);


    //增加供应商
    public boolean addPro(Provider provider);
    // 按Code在数据库里面查
    public Provider queryProviderByProCode(String proCode) throws SQLException;

    // 根据pid 用户编号查询供应商
    public Provider getProviderByPid(int pid) throws SQLException;

    // 根据表单数据更新供应商
    public boolean updateProviderByForm(Provider provider) throws SQLException;

    //删除供应商
    public boolean delProviderByPid(int pid) throws SQLException;

    public List<Provider> getProviderList(String proName, String proCode);


}
