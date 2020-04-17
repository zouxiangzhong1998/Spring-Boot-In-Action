---

## Spring Boot Admin 2.0新特性

Spring Boot Admin 2.0 变化还是挺多的，具体参考 [官网说明](http://codecentric.github.io/spring-boot-admin/current/#_changes_with_2_x)，这里列几条主要的：

- 使用Vue.js重写了UI界面，漂亮得不像实力派

- 直接集成了基于 spring security 的认证，无需引入第三方模块

- 加入 session endpoint 的监控支持

等等...

下面就实际试验来操作感受一下！

---

---

## 搭建 Spring Boot Admin Server

- 创建一个 **SpringBoot 2.0.3 RELEASE** 工程并添加依赖

```
    <dependencies>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
            <version>2.0.1</version>
        </dependency>

        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-server-ui</artifactId>
            <version>2.0.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```

- **应用主类添加注解**

```
@SpringBootApplication
@EnableAdminServer
public class SbaServer20Application {

    public static void main(String[] args) {
        SpringApplication.run(SbaServer20Application.class, args);
    }
}
```

- **启动 Spring Boot Admin Server**

浏览器打开 `localhost:8080`，就可以看到小清新的页面了

![小清新的页面](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/smallFreshPage.jpg)

可以看到这个 UI 的变化和 1.5.X 时代的差距还是蛮大的，此时被监控的应用数目还为0。

接下来我们就来创建一个待监控的Spring Boot 2.0示例。

---

---

## 创建 Spring Boot Admin Client

此处我们依然创建一个 Spring Boot 2.0.3.RELEASE 的应用，然后加入到Spring Boot Admin之中进行监控

- **pom.xml中添加依赖**

```
    <dependencies>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
            <version>2.0.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

```

- 编辑配置文件

```
server.port=8081
spring.application.name=Spring Boot Client
spring.boot.admin.client.url=http://localhost:8080
management.endpoints.web.exposure.include=*
```

- 启动 Spring Boot Admin Client 应用

此时 Spring Boot Admin的页面上应用上线的消息推送过来了：

![应用上线推送](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/appLaunch.jpg)

---

---

## 实际实验

被监控应用上线之后，我们进入 Spring Boot Admin页面鼓捣看看

- **Wallboard 有点小清新**

![Wallboard](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/Wallboard.jpg)

- **Applications 概览**

![Applications概览](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/Applications.jpg)

- **Applications上线日志一目了然**

![Applications上线日志一目了然](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/ApplicationsInfo.jpg)

- **Applications Details**

![Applications Details](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/ApplicationsDetails.jpg)

- **Metrics**

![Metrics](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/Metrics.jpg)

- **Environment**

![Environment](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/Environment.jpg)

- **JMX**

![JMX](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/JMX.jpg)

- **Threads**

![Threads](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/Threads.jpg)

- **Http Traces**

![Http Traces](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/spring_boot_admin2.0/HttpTraces.jpg)

---

---

## 后记


> 由于能力有限，若有错误或者不当之处，还请大家批评指正，一起学习交流！

- 个人博客：https://www.rg-software.com/
- GitHub：https://github.com/zouxiangzhong1998
---
