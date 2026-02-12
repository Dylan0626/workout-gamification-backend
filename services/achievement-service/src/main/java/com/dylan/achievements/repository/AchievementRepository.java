package com.dylan.achievements.repository;

import com.dylan.achievements.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {
    List<Achievement> findByUserIdOrderByAwardedAtDesc(String userId);
    long countByUserId(String userId);
    boolean existsByUserIdAndCode(String userId, String code);
}
