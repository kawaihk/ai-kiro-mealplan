package com.aimealplan.service;

import com.aimealplan.model.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /**
     * 新規ユーザーを登録する。
     *
     * @param userDto     登録するユーザー情報（パスワードを除く）
     * @param rawPassword 平文パスワード（サービス層でハッシュ化される）
     * @return 登録後のユーザー情報
     */
    UserDto createUser(UserDto userDto, String rawPassword);

    /**
     * 全ユーザーの一覧を取得する。
     *
     * @return ユーザー DTO のリスト
     */
    List<UserDto> getAllUsers();

    /**
     * 指定 ID のユーザーを取得する。
     *
     * @param id ユーザー ID
     * @return ユーザー情報（存在しない場合は空）
     */
    Optional<UserDto> getUserById(Long id);

    /**
     * 指定 ID のユーザー情報を更新する。
     *
     * @param id      更新対象のユーザー ID
     * @param userDto 更新するユーザー情報
     * @return 更新後のユーザー情報
     */
    UserDto updateUser(Long id, UserDto userDto);

    /**
     * 指定 ID のユーザーを削除する。
     *
     * @param id 削除対象のユーザー ID
     */
    void deleteUser(Long id);
}
