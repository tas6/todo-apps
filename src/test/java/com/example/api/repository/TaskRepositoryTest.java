package com.example.api.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.api.entity.Task;

public class TaskRepositoryTest {

  @RunWith(SpringRunner.class)
  @SpringBootTest
  @Sql(value = "/sql/delete-all-tasks.sql")
  public static class tasksのデータ件数が0件の場合 {

    @Autowired
    TaskRepository sut;

    @Test
    public void selectAllは空のリストを返す() throws Exception {
      // Exercise
      List<Task> actual = sut.selectAll();
      // Verify
      assertThat(actual.size(), is(0));
    }

    @Test
    public void selectByIdはnullを返す() throws Exception {
      // Exercise
      Task actual = sut.selectById(1);
      // Verify
      assertThat(actual, nullValue());
    }

    @Test
    public void insertは登録したデータを返す() throws Exception {
      // SetUp
      Task task = new Task(null, "件名", "内容", Boolean.FALSE, null);
      // Exercise
      Task actual  = sut.insert(task).getEntity();
      // Verify
      assertThat(actual, is(new Task(Integer.valueOf(1), "件名", "内容", Boolean.FALSE, 1)));
    }

    @Test
    public void insertは最大桁数で登録ができる() throws Exception {
      // SetUp
      Task task = new Task(Integer.MAX_VALUE, StringUtils.repeat("a", 100),
          StringUtils.repeat("b", 500), Boolean.FALSE, Integer.MAX_VALUE);
      // Exercise
      Task actual  = sut.insert(task).getEntity();
      // Verify
      assertThat(actual, is(task));
    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void updateはOptimisticLockingFailureExceptionをスローする() throws Exception {
      // SetUp
      Task task = new Task(1, "件名", "内容", Boolean.FALSE, 0);
      // Exercise
      sut.update(task);
    }

    @Test
    public void deleteは削除件数が0件になる() throws Exception {
      // Exercise
      int actual = sut.delete(1);
      // Verify
      assertThat(actual, is(0));
    }
  }


  @RunWith(SpringRunner.class)
  @SpringBootTest
  @Sql(value = {"/sql/delete-all-tasks.sql", "/sql/insert-initial-tasks.sql"})
  public static class tasksのデータ件数が8件の場合 {

    @Autowired
    TaskRepository sut;

    @Test
    public void selectAllはサイズ8のID順のリストを返す() throws Exception {
      // Exercise
      List<Task> actual = sut.selectAll();
      // Verify
      assertThat(actual.size(), is(8));
      IntStream.rangeClosed(1, 8).forEach(n -> {
        assertThat(actual.get(n - 1).getId(), is(n));
      });
    }

    @Test
    public void selectByIdは指定したIDのデータを返す() throws Exception {
      // Exercise
      Task actual = sut.selectById(1);
      // Verify
      assertThat(actual.getId()         , is(1));
      assertThat(actual.getSubject()    , is("件名1"));
      assertThat(actual.getDescription(), is("本文1"));
      assertThat(actual.getDone()       , is(Boolean.FALSE));
      assertThat(actual.getVersion()    , is(9));
    }

    @Test
    public void insert登録したデータを返す() throws Exception {
      // SetUp
      Task task = new Task(null, "件名", "内容", Boolean.FALSE, null);
      // Exercise
      Task actual = sut.insert(task).getEntity();
      // Verify
      assertThat(actual.getId()         , is(9));
      assertThat(actual.getSubject()    , is("件名"));
      assertThat(actual.getDescription(), is("内容"));
      assertThat(actual.getDone()       , is(Boolean.FALSE));
      assertThat(actual.getVersion()    , is(1));
      assertTrue(actual.equals(sut.selectById(9)));
    }

    @Test
    public void updateで対象データが更新される() throws Exception {
      // SetUp
      Task task = new Task(1, "new件名", "new内容", Boolean.TRUE, 9);
      // Exercise
      Task actual = sut.update(task).getEntity();
      Task selectedTask = sut.selectById(actual.getId());
      // Verify
      assertThat(actual, is(selectedTask));
      assertThat(actual.getId()         , is(1));
      assertThat(actual.getSubject()    , is("new件名"));
      assertThat(actual.getDescription(), is("new内容"));
      assertThat(actual.getDone()       , is(Boolean.TRUE));
      assertThat(actual.getVersion()    , is(10));
      assertTrue(actual.equals(sut.selectById(1)));
    }

    @Test
    public void deleteで対象データが削除される() throws Exception {
      // Exercise
      int count = sut.delete(1);
      Task selectedTask = sut.selectById(1);
      // Verify
      assertThat(count       , is(1));
      assertThat(selectedTask, nullValue());
    }

  }

}
