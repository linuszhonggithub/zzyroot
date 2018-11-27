package com.zzyboot.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zzyboot.entity.ZzyEntityParent;

@Repository
public interface ZzyRepository extends JpaRepository<ZzyEntityParent, Long>, ZzyRepositoryCustom {


}
