package com.carlos.springboot_es_demo.service;

import com.carlos.springboot_es_demo.entity.Entity;

import java.util.List;

public interface TestService {

    void saveEntity(Entity entity);

    void saveEntity(List<Entity> entityList);

    List<Entity> searchEntity(String searchContent);
}
