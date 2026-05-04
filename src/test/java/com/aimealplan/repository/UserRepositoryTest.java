package com.aimealplan.repository;

import com.aimealplan.entity.Role;
import com.aimealplan.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("hashed")
                .role(Role.USER)
                .build());
    }

    // --- findByUsername ---

    @Test
    @DisplayName("findByUsername - 正常系: 存在するユーザー名で User を返す")
    void findByUsername_found() {
        Optional<User> result = userRepository.findByUsername("testuser");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("findByUsername - 異常系: 存在しないユーザー名で Optional.empty を返す")
    void findByUsername_notFound() {
        Optional<User> result = userRepository.findByUsername("unknown");

        assertThat(result).isEmpty();
    }

    // --- findByEmail ---

    @Test
    @DisplayName("findByEmail - 正常系: 存在するメールアドレスで User を返す")
    void findByEmail_found() {
        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("findByEmail - 異常系: 存在しないメールアドレスで Optional.empty を返す")
    void findByEmail_notFound() {
        Optional<User> result = userRepository.findByEmail("unknown@example.com");

        assertThat(result).isEmpty();
    }

    // --- UNIQUE 制約 ---

    @Test
    @DisplayName("save - 異常系: username が重複する場合は例外をスロー")
    void save_duplicateUsername_throwsException() {
        User duplicate = User.builder()
                .username("testuser") // 重複
                .email("other@example.com")
                .password("hashed")
                .role(Role.USER)
                .build();

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> {
                    userRepository.saveAndFlush(duplicate);
                });
    }

    @Test
    @DisplayName("save - 異常系: email が重複する場合は例外をスロー")
    void save_duplicateEmail_throwsException() {
        User duplicate = User.builder()
                .username("otheruser")
                .email("test@example.com") // 重複
                .password("hashed")
                .role(Role.USER)
                .build();

        org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> {
                    userRepository.saveAndFlush(duplicate);
                });
    }
}
