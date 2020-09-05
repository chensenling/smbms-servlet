package com.chensenling.service.user;

import com.chensenling.dao.BaseDao;
import com.chensenling.dao.role.RoleDao;
import com.chensenling.dao.role.RoleDaoImpl;
import com.chensenling.dao.user.UserDao;
import com.chensenling.dao.user.UserDaoImpl;
import com.chensenling.pojo.Role;
import com.chensenling.pojo.User;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl  implements  UserService {

    // 业务层都会调用dao层，所以我们要引入dao层
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();

    }

    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            //通过业务层调用对应的具体的数据库操作
            user = userDao.getLoginUser(connection, userCode);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }

        if (user!=null&& !user.getUserPassword().equals(password)) {
            user = null;

        }

        return user;
    }

    public boolean updatePwd(int id, String pwd) {
        Connection connection = null;
        boolean flag = false;
        connection = BaseDao.getConnection();

        //修改密码
        try {
            if (userDao.updatePwd(connection, id, pwd) > 0) {
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();

            count = userDao.getUserCount(connection, username, userRole);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize) {

        Connection connection = null;
        List<User> userList = null;

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, username, userRole, currentPageNo, pageSize);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }


        return userList;
    }

    public User getUserByUid(int uid) throws SQLException {
        Connection connection=null;
        User user=null;
        connection=BaseDao.getConnection();

        user =userDao.getUserByUid(connection,uid);
        //释放connection
         BaseDao.closeResource(connection,null,null);
        return user;
    }

    public List<Role> getRoleList(){
        Connection connection=null;
        List<Role> roleList=null;
        RoleDao roleDao=new RoleDaoImpl();
        connection = BaseDao.getConnection();

        roleList = roleDao.getRoleList(connection);

        BaseDao.closeResource(connection,null,null);
        return  roleList;
    }
    public boolean updateUserByForm(User user) throws SQLException {
        Connection connection =null;
        int rows=0;
        boolean flag =true;
        connection=BaseDao.getConnection();
        rows =userDao.updateUserByForm(connection,user);

        BaseDao.closeResource(connection,null,null);
        if(rows== 0){
            flag =false;
        }


        return flag;
    }

    //删除用户
    public boolean delUserByUid(int uid) throws SQLException{

        Connection connection=null;
        boolean flag=true;
        int rows=0;

        connection=BaseDao.getConnection();

        rows =userDao.delUserByUid(connection,uid);

        if (rows==0){
            flag=false;
        }
        return flag;
    }

    // 添加用户
    public boolean addUser(User user) throws SQLException {
        Connection connection=null;
        connection=BaseDao.getConnection();
        boolean flag=false;
        int rows=0;

        if (user==null){
            return flag;
        }
        rows =userDao.addUser(connection,user);
        if(rows!=0){
            flag=true;
        }
        BaseDao.closeResource(connection,null,null);
        return flag;
    }
    // 按userCode在数据库里面查
    public User queryUserByUserCode(String userCode) throws SQLException{
        Connection connection=null;
        User user=null;
        connection=BaseDao.getConnection();

        user=userDao.queryUserByUserCode(connection,userCode);

         BaseDao.closeResource(connection,null,null);

         return  user;
    }

/*    @Test
    public void test() throws SQLException {
        User user=new User();
        user.setId(11);
        user.setUserName("李四");
        user.setGender(2);
        user.setBirthday(new Date());
        user.setPhone("18181848403");
        user.setAddress("故事三下辅食锅");
        user.setModifyDate(new Date());
        user.setModifyBy(2);
        user.setCreationDate(new Date());
        user.setCreatedBy(2);
        user.setUserRole(2);
        user.setUserCode("lisi");
//        System.out.println(new UserServiceImpl().queryUserByUserCode(user.getUserCode()).getUserCode());
        System.out.println(new UserServiceImpl().addUser(user));
    }*/

}