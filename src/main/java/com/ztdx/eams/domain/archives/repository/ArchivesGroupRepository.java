package com.ztdx.eams.domain.archives.repository;

import com.ztdx.eams.domain.archives.model.ArchivesGroup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * Created by li on 2018/5/3.
 */
@Repository
@Table(name = "business_classification")
@Qualifier("classificationRepository")
public interface ArchivesGroupRepository extends JpaRepository<ArchivesGroup, Integer>{

}
