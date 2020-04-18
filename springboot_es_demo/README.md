
# 《ElasticSearch搜索引擎在 Spring Boot中的实践》

---

## 实验环境
- ES版本：5.3.0
- spring boot版本：1.5.9

首先当然需要安装好elastic search环境，最好再安装上可视化插件 elasticsearch-head来便于我们直观地查看数据。

当然这部分可以参考codesheep的帖子：
《centos7上elastic search安装填坑记》
https://www.jianshu.com/p/04f4d7b4a1d3

codesheep的ES安装在 http://113.209.119.170:9200/ 这个地址（该地址需要配到springboot项目中去）

---

## Spring工程创建
这部分没有特殊要交代的，但有几个注意点一定要当心

- 注意在新建项目时记得勾选web和NoSQL中的Elasticsearch依赖，来张图说明一下吧：

![创建工程时勾选Nosql中的es依赖选项](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/springboot-es-demo/newProjectAndNosqlandes.jpg)

项目自动生成以后pom.xml中会自动添加`spring-boot-starter-data-elasticsearch`的依赖：
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-elasticsearch</artifactId>
		</dependency>
```

- 本项目中我们使用开源的基于restful的es java客户端`jest`，所以还需要在pom.xml中添加`jest`依赖：
```
		<dependency>
			<groupId>io.searchbox</groupId>
			<artifactId>jest</artifactId>
		</dependency>
```

- 除此之外还必须添加`jna`的依赖：
```
		<dependency>
			<groupId>net.java.dev.jna</groupId>
			<artifactId>jna</artifactId>
		</dependency>
```

否则启动spring项目的时候会报`JNA not found. native methods will be disabled.`的错误：

![JNA not found. native methods will be disabled.](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/springboot-es-demo/JNA_not_found.png)

- 项目的配置文件application.yml中需要把es服务器地址配置对
```
server:
  port: 6325

spring:
  elasticsearch:
    jest:
      uris:
      - http://113.209.119.170:9200  # ES服务器的地址！
      read-timeout: 5000
```

---

## 代码组织

我的项目代码组织如下：
![项目代码组织](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/springboot-es-demo/Project_code_organization.png)

各部分代码详解如下，注释都有：

- Entity.java

```
package com.carlos.springboot_es_demo.entity;
import java.io.Serializable;
import org.springframework.data.elasticsearch.annotations.Document;

public class Entity implements Serializable{

    private static final long serialVersionUID = -763638353551774166L;

    public static final String INDEX_NAME = "index_entity";

    public static final String TYPE = "tstype";

    private Long id;

    private String name;

    public Entity() {
        super();
    }

    public Entity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

```

- TestService.java

```
package com.carlos.springboot_es_demo.service;

import com.carlos.springboot_es_demo.entity.Entity;

import java.util.List;

public interface TestService {

    void saveEntity(Entity entity);

    void saveEntity(List<Entity> entityList);

    List<Entity> searchEntity(String searchContent);
}
```

- TestServiceImpl.java

```

package com.carlos.springboot_es_demo.service.impl;

import java.io.IOException;
import java.util.List;

import com.carlos.springboot_es_demo.entity.Entity;
import com.carlos.springboot_es_demo.service.TestService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServiceImpl.class);

    @Autowired
    private JestClient jestClient;

    @Override
    public void saveEntity(Entity entity) {
        Index index = new Index.Builder(entity).index(Entity.INDEX_NAME).type(Entity.TYPE).build();
        try {
            jestClient.execute(index);
            LOGGER.info("ES 插入完成");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }


    /**
     * 批量保存内容到ES
     */
    @Override
    public void saveEntity(List<Entity> entityList) {
        Bulk.Builder bulk = new Bulk.Builder();
        for(Entity entity : entityList) {
            Index index = new Index.Builder(entity).index(Entity.INDEX_NAME).type(Entity.TYPE).build();
            bulk.addAction(index);
        }
        try {
            jestClient.execute(bulk.build());
            LOGGER.info("ES 插入完成");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 在ES中搜索内容
     */
    @Override
    public List<Entity> searchEntity(String searchContent){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(QueryBuilders.queryStringQuery(searchContent));
        //searchSourceBuilder.field("name");
        searchSourceBuilder.query(QueryBuilders.matchQuery("name",searchContent));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(Entity.INDEX_NAME).addType(Entity.TYPE).build();
        try {
            JestResult result = jestClient.execute(search);
            return result.getSourceAsObjectList(Entity.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

```

- EntityController.java

```
package com.carlos.springboot_es_demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.carlos.springboot_es_demo.entity.Entity;
import com.carlos.springboot_es_demo.service.TestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entityController")
public class EntityController {


    @Autowired
    TestService cityESService;

    @RequestMapping(value="/save", method=RequestMethod.GET)
    public String save(long id, String name) {
        System.out.println("save 接口");
        if(id>0 && StringUtils.isNotEmpty(name)) {
            Entity newEntity = new Entity(id,name);
            List<Entity> addList = new ArrayList<Entity>();
            addList.add(newEntity);
            cityESService.saveEntity(addList);
            return "OK";
        }else {
            return "Bad input value";
        }
    }

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public List<Entity> save(String name) {
        List<Entity> entityList = null;
        if(StringUtils.isNotEmpty(name)) {
            entityList = cityESService.searchEntity(name);
        }
        return entityList;
    }
}
```




---

## 实际实验

增加几条数据，可以使用postman工具，也可以直接在浏览器中输入，如增加以下5条数据：
```
http://localhost:6325/entityController/save?id=1&name=南京中山陵
http://localhost:6325/entityController/save?id=2&name=中国南京师范大学
http://localhost:6325/entityController/save?id=3&name=南京夫子庙
http://localhost:6325/entityController/save?id=4&name=杭州也非常不错
http://localhost:6325/entityController/save?id=5&name=中国南边好像没有叫带京字的城市了
```

数据插入效果如下（使用可视化插件elasticsearch-head观看）：
![数据插入效果](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/springboot-es-demo/Data_insertion_effect.png)


我们来做一下搜索的测试：例如我要搜索关键字“南京”
我们在浏览器中输入：
```
http://localhost:6325/entityController/search?name=南京
```
搜索结果如下：

![关键字“南京”的搜索结果](https://raw.githubusercontent.com/zouxiangzhong1998/assets/master/Spring-Boot-In-Action/springboot-es-demo/NanjingSouso.png)

刚才插入的5条记录中包含关键字“南京”的四条记录均被搜索出来了！

当然这里用的是standard分词方式，将每个中文都作为了一个term，凡是包含“南”、“京”关键字的记录都被搜索了出来，只是评分不同而已，当然还有其他的一些分词方式，此时需要其他分词插件的支持，此处暂不涉及，后文中再做探索。

---

## 后记

> 由于能力有限，若有错误或者不当之处，还请大家批评指正，一起学习交流！

- 个人博客：https://www.rg-software.com/
- GitHub：https://github.com/zouxiangzhong1998

---