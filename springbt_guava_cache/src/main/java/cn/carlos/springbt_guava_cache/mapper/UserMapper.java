package cn.carlos.springbt_guava_cache.mapper;

import cn.carlos.springbt_guava_cache.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> getUsers();

    int addUser(User user);

    List<User> getUsersByName(String userName);
}
