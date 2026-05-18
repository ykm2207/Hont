package com.hont.hont_backend.diet.repository;

import com.hont.hont_backend.diet.entity.DietLogItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietLogItemRepository extends JpaRepository<DietLogItem, Long> {
}
