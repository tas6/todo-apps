package com.example.api.entity;

import static com.example.api.util.ValidateTestUtil.assertViolations;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.example.api.util.ValidateTestUtil.Fixture;

public class TaskTest {

  @RunWith(Theories.class)
  public static class subjectに対するテスト {

    static int MAX_LENGTH = 100;

    Validator validator;
    Task      sut;

    @DataPoints
    public static Fixture<?>[] FIXTURES = {
      // NotEmpty
      new Fixture<String>(null, 1, NotNull.class),
      new Fixture<String>("a" , 0, null),
      // Length
      new Fixture<String>(""  , 1, Length.class),
      new Fixture<String>(StringUtils.repeat("a", MAX_LENGTH)    , 0, null),
      new Fixture<String>(StringUtils.repeat("a", MAX_LENGTH + 1), 1, Length.class)
    };

    @Before
    public void before() throws Exception {
      validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Theory
    public void subjectの入力チェック(Fixture<String> fixture) {
      // SetUp
      Task sut = new Task(1, fixture.getValue(), "内容", false, 0);
      // Exercise, Verify
      assertViolations(validator.validate(sut), fixture);
    }

  }

  @RunWith(Theories.class)
  public static class descriptionに対するテスト {

    static int MAX_LENGTH = 500;

    Validator validator;
    Task      sut;

    @DataPoints
    public static Fixture<?>[] FIXTURES = {
      // NotEmpty
      new Fixture<String>(null, 1, NotNull.class),
      new Fixture<String>("a" , 0, null),
      // Length
      new Fixture<String>(""  , 1, Length.class),
      new Fixture<String>(StringUtils.repeat("a", MAX_LENGTH)    , 0, null),
      new Fixture<String>(StringUtils.repeat("a", MAX_LENGTH + 1), 1, Length.class)
    };

    @Before
    public void before() throws Exception {
      validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Theory
    public void descriptionの入力チェック(Fixture<String> fixture) {
      // SetUp
      Task sut = new Task(1, "件名", fixture.getValue(), false, 0);
      // Exercise, Verify
      assertViolations(validator.validate(sut), fixture);
    }

  }

}
