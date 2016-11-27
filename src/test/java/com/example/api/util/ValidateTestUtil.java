package com.example.api.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.example.api.entity.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ValidateTestUtil {

  /**
   *  Validate結果を検証する.
   *
   * @param violations  バリデーション結果
   * @param fixture     期待値を含むFixture
   */
  public static <T> void assertViolations(Set<ConstraintViolation<T>> violations,
      Fixture<?> fixture) {

    // 件数を確認
    assertThat(violations.size(), is(fixture.errorCount));

    // 意図したエラーであるかを確認
    violations.stream().forEach(v ->
        assertTrue(fixture.errorClass.isInstance(
            v.getConstraintDescriptor().getAnnotation())));
  }

  /**
   * 単項目の入力チェック検証用Fixture
   *
   * @param <T> 入力値の型
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Fixture<T> {
    T         value;
    int       errorCount;
    Class<?>  errorClass;
  }

}
