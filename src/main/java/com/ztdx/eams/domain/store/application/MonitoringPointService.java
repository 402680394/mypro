package com.ztdx.eams.domain.store.application;

import com.ztdx.eams.basic.exception.InvalidArgumentException;
import com.ztdx.eams.domain.store.model.MonitoringPoint;
import com.ztdx.eams.domain.store.repository.MonitoringPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonitoringPointService {

    private final MonitoringPointRepository monitoringPointRepository;

    /**
     * 构造函数
     */
    @Autowired
    public MonitoringPointService(MonitoringPointRepository monitoringPointRepository) {
        this.monitoringPointRepository = monitoringPointRepository;
    }

    /**
     * 新增监测点
     */
    @Transactional
    public void save(MonitoringPoint monitoringPoint) {
        if (monitoringPointRepository.existsByNumber(monitoringPoint.getNumber())){
            throw new InvalidArgumentException("监测点编号已存在");
        }
        if (monitoringPoint.getStatus() == null) {
            monitoringPoint.setStatus(1);
        }
        monitoringPointRepository.save(monitoringPoint);
    }

    /**
     * 修改监测点
     */
    @Transactional
    public void update(MonitoringPoint monitoringPoint) {
        if (monitoringPointRepository.existsByNumber(monitoringPoint.getNumber())){
            throw new InvalidArgumentException("监测点编号已存在");
        }
        monitoringPointRepository.updateById(monitoringPoint);
    }

    /**
     * 删除监测点
     */
    @Transactional
    public void delete(Integer id) {
        if (monitoringPointRepository.existsById(id)){
            monitoringPointRepository.deleteById(id);
        }
    }

}
