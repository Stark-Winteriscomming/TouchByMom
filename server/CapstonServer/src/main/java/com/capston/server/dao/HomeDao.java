package com.capston.server.dao;


import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HomeDao {
	@Autowired
    private SqlSessionTemplate sqlSession;
     
     
    public List<HomeDto> joblistmain() {
        // TODO Auto-generated method stub
        return sqlSession.selectList("sql.joblistmain");
    }
}


