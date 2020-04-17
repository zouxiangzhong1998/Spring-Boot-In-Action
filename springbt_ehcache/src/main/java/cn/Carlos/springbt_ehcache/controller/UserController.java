package cn.carlos.springbt_ehcache.controller;

import cn.codesheep.springbt_ehcache.entity.User;
import cn.codesheep.springbt_ehcache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    CacheManager cacheManager;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/adduser")
    public int addSser() {
        User user = new User();
        user.setUserId(4l);
        user.setUserName("赵四");
        user.setUserAge(38);
        return userService.addUser(user);
    }

    @RequestMapping( value = "/getusersbyname", method = RequestMethod.POST)
    public List<User> geUsersByName( @RequestBody User user ) {
        System.out.println( "-------------------------------------------" );
        System.out.println("call /getusersbyname");
        System.out.println(cacheManager.toString());
        List<User> users = userService.getUsersByName( user.getUserName() );
        return users;
    }

}
