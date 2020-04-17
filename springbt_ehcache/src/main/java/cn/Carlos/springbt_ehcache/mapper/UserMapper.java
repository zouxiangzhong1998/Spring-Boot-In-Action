package cn.carlos.springbt_ehcache.mapper;

import cn.carlos.springbt_ehcache.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> getUsers();

    int addUser(User user);

    List<User> getUsersByName( String userName );
}
