package com.dylan.achievements.controller;

import com.dylan.achievements.dto.AchievementResponse;
import com.dylan.achievements.service.AchievementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    // Service layer dependency that handles all the business logic
    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    /*
     * GET /achievements/{userId}
     * 
     * Returns all achievements awarded to given user
     * Controller returns DTOs instead of raw entity objects
     */
    @GetMapping("/{userId}")
    public List<AchievementResponse> list(@PathVariable String userId) {
        return achievementService.listForUser(userId).stream()
                .map(AchievementResponse::from)
                .toList();
    }
}
