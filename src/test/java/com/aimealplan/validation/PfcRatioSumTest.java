package com.aimealplan.validation;

import jakarta.validation.Constraint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link PfcRatioSum} アノテーション定義のメタアノテーション検証テスト。
 * バリデーションロジックの検証は {@link PfcRatioSumValidatorTest} で行う。
 */
class PfcRatioSumTest {

    @Test
    @DisplayName("@Target - クラス（TYPE）にのみ付与可能であること")
    void target_typeOnly() {
        Target target = PfcRatioSum.class.getAnnotation(Target.class);

        assertThat(target).isNotNull();
        assertThat(target.value()).containsExactly(ElementType.TYPE);
    }

    @Test
    @DisplayName("@Retention - RUNTIME まで保持されること")
    void retention_runtime() {
        Retention retention = PfcRatioSum.class.getAnnotation(Retention.class);

        assertThat(retention).isNotNull();
        assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("@Constraint - PfcRatioSumValidator で検証されること")
    void constraint_validatedByPfcRatioSumValidator() {
        Constraint constraint = PfcRatioSum.class.getAnnotation(Constraint.class);

        assertThat(constraint).isNotNull();
        assertThat(constraint.validatedBy()).containsExactly(PfcRatioSumValidator.class);
    }

    @Test
    @DisplayName("@Documented - Javadoc に含まれること")
    void documented_present() {
        assertThat(PfcRatioSum.class.isAnnotationPresent(Documented.class)).isTrue();
    }

    @Test
    @DisplayName("message - デフォルトメッセージが正しく設定されていること")
    void message_defaultValue() throws NoSuchMethodException {
        String defaultMessage = (String) PfcRatioSum.class
                .getMethod("message")
                .getDefaultValue();

        assertThat(defaultMessage).isEqualTo("PFC比率の合計は100%である必要があります");
    }

    @Test
    @DisplayName("groups - デフォルト値が空配列であること")
    void groups_defaultEmpty() throws NoSuchMethodException {
        Class<?>[] defaultGroups = (Class<?>[]) PfcRatioSum.class
                .getMethod("groups")
                .getDefaultValue();

        assertThat(defaultGroups).isEmpty();
    }

    @Test
    @DisplayName("payload - デフォルト値が空配列であること")
    void payload_defaultEmpty() throws NoSuchMethodException {
        Class<?>[] defaultPayload = (Class<?>[]) PfcRatioSum.class
                .getMethod("payload")
                .getDefaultValue();

        assertThat(defaultPayload).isEmpty();
    }
}
