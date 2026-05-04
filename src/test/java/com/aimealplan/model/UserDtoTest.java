package com.aimealplan.model;

import com.aimealplan.entity.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // UserDto はバリデーションアノテーションを持たないため、
    // Builder パターンによるオブジェクト生成と Lombok @Data の動作を検証する。

    @Test
    @DisplayName("builder - 全フィールドを設定して正しく生成できること")
    void builder_allFields() {
        UserDto dto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(Role.USER)
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("testuser");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("builder - フィールド未設定で null が返ること")
    void builder_defaultNull() {
        UserDto dto = UserDto.builder().build();

        assertThat(dto.getId()).isNull();
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getEmail()).isNull();
        assertThat(dto.getRole()).isNull();
    }

    @Test
    @DisplayName("setter - setUsername で値を更新できること")
    void setter_username() {
        UserDto dto = UserDto.builder().username("original").build();
        dto.setUsername("updated");

        assertThat(dto.getUsername()).isEqualTo("updated");
    }

    @Test
    @DisplayName("NoArgsConstructor + setter - 引数なしコンストラクタで生成後に全フィールドを設定できること")
    void noArgsConstructor_withSetters() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setRole(Role.ADMIN);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("testuser");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("equals - null との比較は等価でないこと")
    void equals_null_notEqual() {
        UserDto dto = UserDto.builder().id(1L).username("testuser").build();

        assertThat(dto).isNotEqualTo(null);
    }

    @Test
    @DisplayName("equals - 異なる型との比較は等価でないこと")
    void equals_differentType_notEqual() {
        UserDto dto = UserDto.builder().id(1L).username("testuser").build();

        assertThat(dto).isNotEqualTo("testuser");
    }

    @Test
    @DisplayName("equals - id が異なる場合は等価でないこと")
    void equals_differentId_notEqual() {
        UserDto dto1 = UserDto.builder().id(1L).username("testuser").build();
        UserDto dto2 = UserDto.builder().id(2L).username("testuser").build();

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("equals - email が異なる場合は等価でないこと")
    void equals_differentEmail_notEqual() {
        UserDto dto1 = UserDto.builder().id(1L).email("a@example.com").build();
        UserDto dto2 = UserDto.builder().id(1L).email("b@example.com").build();

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("equals - role が異なる場合は等価でないこと")
    void equals_differentRole_notEqual() {
        UserDto dto1 = UserDto.builder().id(1L).role(Role.USER).build();
        UserDto dto2 = UserDto.builder().id(1L).role(Role.ADMIN).build();

        assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("toString - 全フィールドが含まれること")
    void toString_containsAllFields() {
        UserDto dto = UserDto.builder()
                .id(1L).username("testuser").email("test@example.com").role(Role.USER).build();
        String str = dto.toString();

        assertThat(str).contains("id=1");
        assertThat(str).contains("username=testuser");
        assertThat(str).contains("email=test@example.com");
        assertThat(str).contains("role=USER");
    }

    @Test
    @DisplayName("equals - 同一フィールド値を持つ DTO は等価であること")
    void equals_sameFields() {
        UserDto dto1 = UserDto.builder().id(1L).username("testuser").email("test@example.com").role(Role.USER).build();
        UserDto dto2 = UserDto.builder().id(1L).username("testuser").email("test@example.com").role(Role.USER).build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("バリデーション - UserDto にはバリデーションアノテーションがなくエラーが発生しないこと")
    void validation_noConstraints() {
        UserDto dto = UserDto.builder().build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
