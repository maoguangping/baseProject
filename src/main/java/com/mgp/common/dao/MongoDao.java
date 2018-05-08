package com.mgp.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository("mongoDao")
public class MongoDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Description: 插入data
     * @param data
     */
    public void insertData(String data) {
        mongoTemplate.insert(data);
    }

    public String getData(String collenctionName) {
        return mongoTemplate.getDb().getCollection(collenctionName).findOne().toString();
    }
}
