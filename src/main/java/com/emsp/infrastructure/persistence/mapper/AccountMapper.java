package com.emsp.infrastructure.persistence.mapper;

import com.emsp.infrastructure.persistence.po.AccountPO;
import org.apache.ibatis.annotations.*;

import java.time.Instant;
import java.util.List;

@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO accounts (email, status, created_date, last_updated) " +
            "VALUES (#{email}, #{status}, #{createdDate}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AccountPO account);
    
    @Update("UPDATE accounts SET " +
            "email = #{email}, " +
            "status = #{status}, " +
            "last_updated = #{lastUpdated} " +
            "WHERE id = #{id}")
    void update(AccountPO account);
    
    @Select("SELECT * FROM accounts WHERE id = #{id}")
    AccountPO selectById(Long id);
    
    @Select("SELECT * FROM accounts WHERE email = #{email}")
    AccountPO selectByEmail(String email);
    
    @Select("SELECT * FROM accounts WHERE last_updated >= #{lastUpdated} " +
            "ORDER BY last_updated DESC " +
            "LIMIT #{size} OFFSET #{offset}")
    List<AccountPO> selectByLastUpdatedAfter(
            @Param("lastUpdated") Instant lastUpdated,
            @Param("offset") int offset,
            @Param("size") int size);
}