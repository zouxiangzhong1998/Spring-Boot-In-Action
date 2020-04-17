package cn.carlos.springbt_security_jwt.service;

import cn.carlos.springbt_security_jwt.model.entity.User;

public interface AuthService {

    User register( User userToAdd );
    String login( String username, String password );
}
