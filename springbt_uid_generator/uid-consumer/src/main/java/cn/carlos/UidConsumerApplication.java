package cn.carlos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"cn.carlos","com.baidu.fsg.uid.worker.dao"})
public class UidConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UidConsumerApplication.class,args);
    }
}
