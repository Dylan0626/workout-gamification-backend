package com.dylan.achievements.repository;

import com.dylan.achievements.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatsRepository extends JpaRepository<UserStats, String> {
}
