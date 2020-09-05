package com.chensenling.dao.user;

import com.chensenling.dao.BaseDao;
import com.chensenling.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
//得到登录用户
    public User getLoginUser(Connection connection, String userCode)  {
        ResultSet rs = null;
        PreparedStatement pre = null;
        String sql=null;
        User user =null;
        if(connection != null){
            sql = "select * from smbms_user where userCode= ?";
            Object[] params = {userCode};

            try {
                rs= BaseDao.execute(connection,sql,params,rs,pre);

                // 遍历rs
                if(rs.next()){
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getDate("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getDate("modifyDate"));

                }

                BaseDao.closeResource(null,rs,pre);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return user;
    }

//    修改当前用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException {

        PreparedStatement pre =null;
        int rows=0;
        if(connection!=null){
            String sql= "update smbms_user set userPassword=? where id=?";
            Object[] params={password,id};
            rows =BaseDao.execute(connection,sql,params,pre);
            //释放资源
            BaseDao.closeResource(null,null,pre);

        }


        return rows;
    }

// 得到用户列表
    public List<User> getUserList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws SQLException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<User> userList=new ArrayList<User>();
        User user =null;



        if(connection!=null){
            StringBuffer sql =new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");

            List<Object> list=new ArrayList<Object>();

            if(!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }


            if(userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }


            sql.append(" order by creationDate DESC limit ?,?");


            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params =list.toArray();


            resultSet = BaseDao.execute(connection,sql.toString(),params,resultSet,preparedStatement);
            while (resultSet.next()){
                user=new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));
                user.setUserRoleName(resultSet.getString("userRoleName"));

                userList.add(user);


            }

            BaseDao.closeResource(null,resultSet,preparedStatement);

        }

        return userList;
    }

    // 根据用户名或角色名查询用户数
    public int getUserCount(Connection connection, String username, int userrole) throws SQLException {
        PreparedStatement pre=null;
        ResultSet result=null;
        int count=0;
        if(connection!=null){
            //多个sql拼接，使用stringbuffer
            StringBuffer sql =new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole =r.id");

            ArrayList<Object> list = new ArrayList<Object>();
             if(!StringUtils.isNullOrEmpty(username)){
                 sql.append(" and u.userName like ?");
                 list.add("%"+username+"%");
             }
            if(userrole>0){
                sql.append(" and u.userRole = ?");
                list.add(userrole);
            }
            System.out.println(sql.toString());
            //怎么把list转为数组
            Object[] objs =list.toArray();

           result= BaseDao.execute(connection,sql.toString(),objs,result,pre);
           while(result.next()){
              count =result.getInt("count");

           }
           BaseDao.closeResource(null,result,pre);

        }

        return count;
    }
// 根据用户id,获取用户信息
    public User getUserByUid(Connection connection, int uid) throws SQLException {

        PreparedStatement pre=null;
        ResultSet resultSet =null;
        User user =null;
        if(connection!=null){

            String sql="select u.*,r.roleName userRoleName from smbms_user u,smbms_role r where u.userRole=r.id and u.id=?";
            Object[] params={uid};

            resultSet =BaseDao.execute(connection,sql,params,resultSet,pre);
            while (resultSet.next()){
                user=new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));
                user.setUserRoleName(resultSet.getString("userRoleName"));


            }
            // 释放资源
            BaseDao.closeResource(null,resultSet,pre);



        }
        return user;
    }
// 根据表单数据更新用户
    public int updateUserByForm(Connection connection, User newuser) throws SQLException {

        PreparedStatement pre =null;
        int rows =0;
         if(connection!=null){

             String sql ="update smbms_user u set u.userName=? ,u.gender=?,u.birthday=?,u.phone=?,u.address=?,u.userRole=?,u.modifyBy=?,u.modifyDate=? where u.id=?";
            Object[] params ={newuser.getUserName(),newuser.getGender(),newuser.getBirthday(),newuser.getPhone(),newuser.getAddress(),newuser.getUserRole(),newuser.getModifyBy(),newuser.getModifyDate(),newuser.getId()};
             System.out.println(params[params.length-1]);//
            rows= BaseDao.execute(connection,sql,params,pre);

            BaseDao.closeResource(null,null,pre);

         }
        return rows;
    }

    //删除用户
    public int delUserByUid(Connection connection,int uid) throws SQLException {
        PreparedStatement pre=null;
        int rows=0;

        if(connection!=null){
            String sql ="delete from smbms_user  where smbms_user.id= ?";
            Object[] params={uid};
             rows = BaseDao.execute(connection, sql, params, pre);
        }
        BaseDao.closeResource(null,null,pre);
        return  rows;
    }

    // 添加用户
    public int addUser(Connection connection,User user)  {
        PreparedStatement pre=null;
        int rows=0;
        if(connection!=null) {

            String sql = "insert into  smbms_user values(null,?,?,?,?,?,?,?,?,?,?,?,?)";

            System.out.println(sql.toString());
            ArrayList<Object> list = new ArrayList<Object>();
            list.add(user.getUserCode());
            list.add(user.getUserName());
            list.add(user.getUserPassword());
            list.add(user.getGender());
            list.add(user.getBirthday());
            list.add(user.getPhone());
            list.add(user.getAddress());
            list.add(user.getUserRole());
            list.add(user.getCreatedBy());
            list.add(user.getCreationDate());
            list.add(user.getModifyBy());
            list.add(user.getModifyDate());

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
    // 按userCode在数据库里面查
    public User queryUserByUserCode(Connection connection,String userCode) throws SQLException {

        PreparedStatement pre =null;
        ResultSet resultSet=null;
        int rows=0;
        User user=null;

        String sql="select u.userCode from smbms_user u where u.userCode=? ";
        Object[] params={userCode};
        resultSet = BaseDao.execute(connection,sql,params,resultSet,pre);
        while(resultSet.next()){
            user=new User();
            user.setUserCode(resultSet.getString("userCode"));
        }
        BaseDao.closeResource(null,resultSet,pre);
        return user;
    }

}
