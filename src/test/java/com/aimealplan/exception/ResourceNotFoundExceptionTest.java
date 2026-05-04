package com.aimealplan.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("メッセージが正しく伝播されること")
    void constructor_messageIsPropagated() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recipe not found with id: 99");

        assertThat(ex.getMessage()).isEqualTo("Recipe not found with id: 99");
    }

    @Test
    @DisplayName("RuntimeException を継承していること")
    void isRuntimeException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("test");

        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("スローされた例外のメッセージを catch で取得できること")
    void thrownException_canBeCaught() {
        assertThatThrownBy(() -> {
            throw new ResourceNotFoundException("User not found with id: 1");
        })
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found with id: 1");
    }
}
