package com.aimealplan.service.impl;

import com.aimealplan.entity.User;
import com.aimealplan.entity.UserGoal;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserGoalDto;
import com.aimealplan.repository.UserGoalRepository;
import com.aimealplan.repository.UserRepository;
import com.aimealplan.service.UserGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGoalServiceImpl implements UserGoalService {

    private final UserGoalRepository userGoalRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserGoalDto createUserGoal(UserGoalDto userGoalDto) {
        Long userId = userGoalDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        UserGoal userGoal = toEntity(userGoalDto, user);
        return toDto(userGoalRepository.save(userGoal));
    }

    @Override
    public Optional<UserGoalDto> getUserGoalByUserId(Long userId) {
        // ユーザー存在確認は行わず、目標の有無のみを返す。
        // ユーザーが存在しない場合も目標未設定と同様に Optional.empty() を返し、
        // コントローラ側で一律 404 として扱う。
        return userGoalRepository.findByUserId(userId).map(this::toDto);
    }

    @Override
    @Transactional
    public UserGoalDto updateUserGoal(Long userId, UserGoalDto userGoalDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        UserGoal userGoal = userGoalRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserGoal not found for userId: " + userId));

        userGoal.setTargetCalories(userGoalDto.getTargetCalories());
        userGoal.setTargetWeight(userGoalDto.getTargetWeight());
        userGoal.setActivityLevel(userGoalDto.getActivityLevel());
        userGoal.setTargetProtein(userGoalDto.getProteinRatio());
        userGoal.setTargetFat(userGoalDto.getFatRatio());
        userGoal.setTargetCarbohydrates(userGoalDto.getCarbohydrateRatio());

        return toDto(userGoalRepository.save(userGoal));
    }

    @Override
    @Transactional
    public void deleteUserGoal(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        UserGoal userGoal = userGoalRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "UserGoal not found for userId: " + userId));
        userGoalRepository.delete(userGoal);
    }

    // --- マッピングヘルパー ---

    private UserGoal toEntity(UserGoalDto dto, User user) {
        return UserGoal.builder()
                .user(user)
                .targetCalories(dto.getTargetCalories())
                .targetWeight(dto.getTargetWeight())
                .activityLevel(dto.getActivityLevel())
                .targetProtein(dto.getProteinRatio())
                .targetFat(dto.getFatRatio())
                .targetCarbohydrates(dto.getCarbohydrateRatio())
                .build();
    }

    private UserGoalDto toDto(UserGoal entity) {
        UserGoalDto dto = new UserGoalDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setTargetCalories(entity.getTargetCalories());
        dto.setTargetWeight(entity.getTargetWeight());
        dto.setActivityLevel(entity.getActivityLevel());
        dto.setProteinRatio(entity.getTargetProtein());
        dto.setFatRatio(entity.getTargetFat());
        dto.setCarbohydrateRatio(entity.getTargetCarbohydrates());
        return dto;
    }
}
