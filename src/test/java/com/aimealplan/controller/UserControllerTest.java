package com.aimealplan.controller;

import com.aimealplan.entity.Role;
import com.aimealplan.exception.DuplicateResourceException;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.UserCreateRequest;
import com.aimealplan.model.UserDto;
import com.aimealplan.model.UserUpdateRequest;
import com.aimealplan.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateRequest buildCreateRequest() {
        UserCreateRequest req = new UserCreateRequest();
        req.setUsername("testuser");
        req.setPassword("password123");
        req.setEmail("test@example.com");
        req.setRole("USER");
        return req;
    }

    private UserDto buildUserDto(Long id) {
        return UserDto.builder().id(id).username("testuser").email("test@example.com").role(Role.USER).build();
    }

    // --- POST /api/users ---

    @Test
    @DisplayName("POST /api/users - ユーザー登録 正常系: 201 Created を返す")
    void createUser_success() throws Exception {
        when(userService.createUser(any(UserDto.class), eq("password123"))).thenReturn(buildUserDto(1L));

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("POST /api/users - ユーザー登録 異常系: username未入力で 400 を返す")
    void createUser_validationError_blankUsername() throws Exception {
        UserCreateRequest req = buildCreateRequest();
        req.setUsername("");

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/users - ユーザー登録 異常系: 不正なロール値で 400 を返す")
    void createUser_validationError_invalidRole() throws Exception {
        UserCreateRequest req = buildCreateRequest();
        req.setRole("SUPERUSER");

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/users - ユーザー登録 異常系: username重複で 409 を返す")
    void createUser_duplicateUsername() throws Exception {
        when(userService.createUser(any(UserDto.class), any()))
                .thenThrow(new DuplicateResourceException("Username already in use: testuser"));

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildCreateRequest())))
                .andExpect(status().isConflict());
    }

    // --- GET /api/users ---

    @Test
    @DisplayName("GET /api/users - 全ユーザー取得 正常系: 200 OK を返す")
    void getAllUsers_success() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(buildUserDto(1L)));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // --- GET /api/users/{id} ---

    @Test
    @DisplayName("GET /api/users/{id} - ID指定取得 正常系: 200 OK を返す")
    void getUserById_success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(buildUserDto(1L)));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /api/users/{id} - ID指定取得 異常系: 存在しない場合 404 を返す")
    void getUserById_notFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    // --- PUT /api/users/{id} ---

    @Test
    @DisplayName("PUT /api/users/{id} - ユーザー更新 正常系: 200 OK を返す")
    void updateUser_success() throws Exception {
        UserUpdateRequest req = new UserUpdateRequest();
        req.setUsername("updated");
        req.setEmail("updated@example.com");
        req.setRole("ADMIN");

        UserDto response = UserDto.builder().id(1L).username("updated").email("updated@example.com").role(Role.ADMIN).build();
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated"));
    }

    @Test
    @DisplayName("PUT /api/users/{id} - ユーザー更新 異常系: 存在しない場合 404 を返す")
    void updateUser_notFound() throws Exception {
        UserUpdateRequest req = new UserUpdateRequest();
        req.setUsername("updated");
        req.setEmail("updated@example.com");

        when(userService.updateUser(eq(99L), any(UserDto.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 99"));

        mockMvc.perform(put("/api/users/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/users/{id} - ユーザー更新 異常系: email重複で 409 を返す")
    void updateUser_duplicateEmail() throws Exception {
        UserUpdateRequest req = new UserUpdateRequest();
        req.setUsername("updated");
        req.setEmail("dup@example.com");

        when(userService.updateUser(eq(1L), any(UserDto.class)))
                .thenThrow(new DuplicateResourceException("Email already in use: dup@example.com"));

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    // --- DELETE /api/users/{id} ---

    @Test
    @DisplayName("DELETE /api/users/{id} - ユーザー削除 正常系: 204 No Content を返す")
    void deleteUser_success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - ユーザー削除 異常系: 存在しない場合 404 を返す")
    void deleteUser_notFound() throws Exception {
        doThrow(new ResourceNotFoundException("User not found with id: 99"))
                .when(userService).deleteUser(99L);

        mockMvc.perform(delete("/api/users/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
