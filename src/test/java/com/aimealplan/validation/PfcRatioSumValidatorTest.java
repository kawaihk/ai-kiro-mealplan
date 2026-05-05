package com.aimealplan.validation;

import com.aimealplan.model.UserGoalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PfcRatioSumValidatorTest {

    private PfcRatioSumValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PfcRatioSumValidator();
    }

    private UserGoalDto buildDto(Integer protein, Integer fat, Integer carb) {
        UserGoalDto dto = new UserGoalDto();
        dto.setUserId(1L);
        dto.setProteinRatio(protein);
        dto.setFatRatio(fat);
        dto.setCarbohydrateRatio(carb);
        return dto;
    }

    // --- 正常系 ---

    @Test
    @DisplayName("isValid - 正常系: PFC合計が100の場合は true を返す")
    void isValid_sumEquals100() {
        assertThat(validator.isValid(buildDto(30, 30, 40), null)).isTrue();
    }

    @Test
    @DisplayName("isValid - 正常系: 全フィールドが null（未設定）の場合は true を返す")
    void isValid_allNull() {
        assertThat(validator.isValid(buildDto(null, null, null), null)).isTrue();
    }

    @Test
    @DisplayName("isValid - 正常系: dto 自体が null の場合は true を返す")
    void isValid_dtoNull() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    @DisplayName("isValid - 正常系: 均等配分（33+33+34=100）で true を返す")
    void isValid_evenDistribution() {
        assertThat(validator.isValid(buildDto(33, 33, 34), null)).isTrue();
    }

    // --- 異常系 ---

    @Test
    @DisplayName("isValid - 異常系: PFC合計が100を超える場合は false を返す")
    void isValid_sumOver100() {
        assertThat(validator.isValid(buildDto(40, 40, 40), null)).isFalse();
    }

    @Test
    @DisplayName("isValid - 異常系: PFC合計が100未満の場合は false を返す")
    void isValid_sumUnder100() {
        assertThat(validator.isValid(buildDto(20, 20, 20), null)).isFalse();
    }

    @Test
    @DisplayName("isValid - 異常系: 一部が null で合計が100にならない場合は false を返す")
    void isValid_partialNull_sumNot100() {
        // protein=50, fat=null(0), carb=30 → 合計80
        assertThat(validator.isValid(buildDto(50, null, 30), null)).isFalse();
    }

    @Test
    @DisplayName("isValid - 正常系: 一部が null でも残りの合計が100の場合は true を返す")
    void isValid_partialNull_sumEquals100() {
        // protein=100, fat=null(0), carb=null(0) → 合計100
        assertThat(validator.isValid(buildDto(100, null, null), null)).isTrue();
    }

    @Test
    @DisplayName("isValid - 正常系: fat のみ設定で合計100の場合は true を返す")
    void isValid_onlyFat_sumEquals100() {
        // protein=null(0), fat=100, carb=null(0) → 合計100
        assertThat(validator.isValid(buildDto(null, 100, null), null)).isTrue();
    }

    @Test
    @DisplayName("isValid - 正常系: carb のみ設定で合計100の場合は true を返す")
    void isValid_onlyCarb_sumEquals100() {
        // protein=null(0), fat=null(0), carb=100 → 合計100
        assertThat(validator.isValid(buildDto(null, null, 100), null)).isTrue();
    }

    @Test
    @DisplayName("isValid - 異常系: fat のみ設定で合計が100未満の場合は false を返す")
    void isValid_onlyFat_sumNot100() {
        // protein=null(0), fat=50, carb=null(0) → 合計50
        assertThat(validator.isValid(buildDto(null, 50, null), null)).isFalse();
    }

    @Test
    @DisplayName("isValid - 異常系: carb のみ設定で合計が100未満の場合は false を返す")
    void isValid_onlyCarb_sumNot100() {
        // protein=null(0), fat=null(0), carb=70 → 合計70
        assertThat(validator.isValid(buildDto(null, null, 70), null)).isFalse();
    }

    @Test
    @DisplayName("isValid - 異常系: 全て0の場合は false を返す（未設定ではなく明示的に0を設定）")
    void isValid_allZero() {
        assertThat(validator.isValid(buildDto(0, 0, 0), null)).isFalse();
    }
}
