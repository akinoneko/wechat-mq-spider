package com.zyy.wechat.mq.spider.dao;

import com.zyy.wechat.mq.spider.entity.SpiderQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by akinoneko on 2017/3/25.
 */
public interface SpiderQueueRepository extends CrudRepository<SpiderQueue, Integer> {

    @Modifying
    @Query("delete from SpiderQueue where loading = 1")
    public void deleteLoading();

    /**
     * 分页查询队列
     *
     * @param pageable 分页参数
     * @return
     */
    @Query("select s from SpiderQueue s")
    public Page<SpiderQueue> findByPage(Pageable pageable);

    @Query("select s from SpiderQueue s where contentUrl like CONCAT('%',:sn,'%')")
    public void deleteBySn(@Param("sn") String sn);
}
