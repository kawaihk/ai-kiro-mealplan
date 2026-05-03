package com.aimealplan.service;

import com.aimealplan.model.UserGoalDto;

import java.util.Optional;

public interface UserGoalService {

    /**
     * ユーザーの目標栄養素設定を登録する。
     *
     * @param userGoalDto 登録する目標栄養素情報
     * @return 登録後の目標栄養素情報
     */
    UserGoalDto createUserGoal(UserGoalDto userGoalDto);

    /**
     * 指定ユーザーの目標栄養素設定を取得する。
     *
     * @param userId ユーザーID
     * @return 目標栄養素情報（存在しない場合は空）
     */
    Optional<UserGoalDto> getUserGoalByUserId(Long userId);

    /**
     * 指定ユーザーの目標栄養素設定を更新する。
     *
     * @param userId      ユーザーID
     * @param userGoalDto 更新する目標栄養素情報
     * @return 更新後の目標栄養素情報
     */
    UserGoalDto updateUserGoal(Long userId, UserGoalDto userGoalDto);

    /**
     * 指定ユーザーの目標栄養素設定を削除する。
     *
     * @param userId ユーザーID
     */
    void deleteUserGoal(Long userId);
}
