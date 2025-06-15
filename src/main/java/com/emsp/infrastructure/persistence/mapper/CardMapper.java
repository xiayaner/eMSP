package com.emsp.infrastructure.persistence.mapper;

import com.emsp.infrastructure.persistence.po.CardPO;
import org.apache.ibatis.annotations.*;

import java.time.Instant;
import java.util.List;

@Mapper
public interface CardMapper {
    @Insert("INSERT INTO cards (id, uid, visible_number, contract_id, status, account_id, created_date, last_updated) " +
            "VALUES (#{id}, #{uid}, #{visibleNumber}, #{contractId}, #{status}, #{accountId}, #{createdDate}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CardPO card);
    
    @Update("UPDATE cards SET " +
            "uid = #{uid}, " +
            "visible_number = #{visibleNumber}, " +
            "contract_id = #{contractId}, " +
            "status = #{status}, " +
            "account_id = #{accountId}, " +
            "last_updated = #{lastUpdated} " +
            "WHERE id = #{id}")
    void update(CardPO card);
    
    @Select("SELECT * FROM cards WHERE id = #{id}")
    CardPO selectById(String id);
    
    @Select("SELECT * FROM cards WHERE uid = #{uid}")
    CardPO selectByUid(String uid);
    
    @Select("SELECT * FROM cards WHERE account_id = #{accountId}")
    List<CardPO> selectByAccountId(Long accountId);
    
    @Select("SELECT * FROM cards WHERE last_updated >= #{lastUpdated} " +
            "ORDER BY last_updated DESC " +
            "LIMIT #{size} OFFSET #{offset}")
    List<CardPO> selectByLastUpdatedAfter(
            @Param("lastUpdated") Instant lastUpdated,
            @Param("offset") int offset,
            @Param("size") int size);


    @Select({
            "<script>",
            "SELECT * FROM cards",
            "WHERE account_id IN",
            "<foreach item='id' collection='accountIds' open='(' separator=',' close=')'>",
                "#{id}",
            "</foreach>",
            "</script>"
    })
    List<CardPO> selectByAccountIds(@Param("accountIds") List<Long> accountIds);
}