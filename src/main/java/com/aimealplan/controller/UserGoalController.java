package com.aimealplan.controller;

import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserGoalDto;
import com.aimealplan.service.UserGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/goal")
@RequiredArgsConstructor
public class UserGoalController {

    private final UserGoalService userGoalService;

    /**
     * ユーザーの目標栄養素設定を登録する。
     *
     * @param userId      ユーザーID
     * @param userGoalDto 登録する目標栄養素情報
     * @return 登録後の目標栄養素情報（201 Created）
     */
    @PostMapping
    public ResponseEntity<UserGoalDto> createUserGoal(
            @PathVariable Long userId,
            @Valid @RequestBody UserGoalDto userGoalDto) {
        userGoalDto.setUserId(userId);
        UserGoalDto created = userGoalService.createUserGoal(userGoalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * ユーザーの目標栄養素設定を取得する。
     *
     * @param userId ユーザーID
     * @return 目標栄養素情報（200 OK）、存在しない場合は 404 Not Found
     */
    @GetMapping
    public ResponseEntity<UserGoalDto> getUserGoal(@PathVariable Long userId) {
        return userGoalService.getUserGoalByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserGoal not found for userId: " + userId));
    }

    /**
     * ユーザーの目標栄養素設定を更新する。
     *
     * @param userId      ユーザーID
     * @param userGoalDto 更新する目標栄養素情報
     * @return 更新後の目標栄養素情報（200 OK）
     */
    @PutMapping
    public ResponseEntity<UserGoalDto> updateUserGoal(
            @PathVariable Long userId,
            @Valid @RequestBody UserGoalDto userGoalDto) {
        UserGoalDto updated = userGoalService.updateUserGoal(userId, userGoalDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * ユーザーの目標栄養素設定を削除する。
     *
     * @param userId ユーザーID
     * @return 204 No Content
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUserGoal(@PathVariable Long userId) {
        userGoalService.deleteUserGoal(userId);
        return ResponseEntity.noContent().build();
    }
}
