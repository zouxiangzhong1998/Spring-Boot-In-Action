package cn.carlos.springbt_mybatis_sqlserver.service;

import cn.carlos.springbt_mybatis_sqlserver.entity.User;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();
    int addUser( User user );
    int deleteUser( User user );
}
