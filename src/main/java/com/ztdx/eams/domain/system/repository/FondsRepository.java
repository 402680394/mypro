package com.ztdx.eams.domain.system.repository;

import com.ztdx.eams.domain.system.model.Fonds;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

/**
 * Created by li on 2018/4/11.
 */
@Repository
@Table(name = "sys_fonds")
@Qualifier("fondsRepository")
public interface FondsRepository extends JpaRepository<Fonds, Integer> {

    boolean existsByCodeAndGmtDeleted(String code, boolean gmtDeleted);

    boolean existsByCodeAndId(String code, int id);

    //查询ID是否存在
    boolean existsById(int id);

    //查询同级全宗优先级最大值
    @Query("select max (f.orderNumber) from Fonds f where f.parentId=:parentId")
    Integer findMaxOrderNumber(@Param(value = "parentId") int parentId);

    // 通过父ID查询是否存在子全宗
    boolean existsByParentId(int id);

    //通过ID查询
    Fonds findById(int id);

    //查询所有的全宗
    List<Fonds> findByParentIdNotNull();

    //通过ID修改
    @Modifying
    @Query("update Fonds f set f.parentId=:#{#fonds.parentId},f.code=:#{#fonds.code},f.name=:#{#fonds.name},f.remark=:#{#fonds.remark} where f.id=:#{#fonds.id}")
    void updateById(@Param(value = "fonds") Fonds fonds);

    //设置优先级
    @Modifying
    @Query("update Fonds f set f.orderNumber=:orderNumber where f.id=:id")
    void updateOrderNumberById(@Param(value = "id") int id, @Param(value = "orderNumber") int orderNumber);

    //设置状态为删除
    @Modifying
    @Query("update Fonds f set f.gmtCreate=:gmtCreate where f.id=:id")
    void updateGmtDeletedById(@Param(value = "id") int id, @Param(value = "gmtCreate") boolean gmtCreate);
}
