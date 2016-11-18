package com.example.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.seasar.doma.jdbc.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.api.entity.Task;
import com.example.api.exception.InvalidVersionException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.TaskRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Autowired
  @InjectMocks
  TaskService sut;

  @Mock
  TaskRepository repository;

  @Mock
  Result<Task> result;

  @Test
  public void selectAllはリポジトリが返したデータを返す() throws Exception {
    // SetUp
    List<Task> expected = new ArrayList<>();
    expected.add(new Task(1, "件名", "内容", Boolean.FALSE, 1));
    when(repository.selectAll()).thenReturn(expected);
    // Exercise
    List<Task> actual = sut.selectAll();
    // Verify
    assertThat(actual, is(expected));
  }

  @Test
  public void selectAllはデータがない場合に空のリストを返す() throws Exception {
    // SetUp
    when(repository.selectAll()).thenReturn(new ArrayList<Task>());
    // Exercise
    List<Task> tasks = sut.selectAll();
    // Verify
    assertThat(tasks.size(), is(0));
  }

  @Test()
  public void selectByIdはリポジトリが返したデータを返す() throws Exception {
    // SetUp
    Task expected = new Task(1, "件名", "内容", Boolean.TRUE, 3);
    when(repository.selectById(1)).thenReturn(expected);
    // Exercise
    Task actual = sut.selectById(1);
    // Verify
    assertThat(actual, is(expected));
    verify(repository).selectById(1);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void selectByIdはデータがない場合に例外をスローする() throws Exception {
    // SetUp
    when(repository.selectById(1)).thenReturn(null);
    // Exercise
    sut.selectById(1);
  }

  @Test
  public void addは登録したデータを返す() throws Exception {
    // SetUp
    Task task     = new Task(null, "件名", "内容", Boolean.TRUE, null);
    Task expected = new Task(1   , "件名", "内容", Boolean.TRUE, 1);
    when(result.getEntity()     ).thenReturn(expected);
    when(repository.insert(task)).thenReturn(result);
    // Exercise
    Task actual = sut.add(task);
    // Verify
    assertThat(actual, is(expected));
    verify(repository).insert(task);
  }

  @Test
  public void editは更新したデータを返す() throws Exception {
    // SetUp
    Task task     = new Task(1, "件名" , "内容" , Boolean.TRUE , 0);
    Task expected = new Task(2, "件名2", "内容2", Boolean.FALSE, 0);
    when(result.getCount()      ).thenReturn(1);
    when(result.getEntity()     ).thenReturn(expected);
    when(repository.update(task)).thenReturn(result);
    // Exercise
    Task actual = sut.edit(task);
    // Verify
    assertThat(actual, is(expected));
    verify(repository).update(task);
  }

  @Test(expected = InvalidVersionException.class)
  public void editは楽観的排他ロックエラーの場合に例外をスローする() throws Exception {
    // SetUp
    Task task = new Task(1, "件名", "内容", Boolean.TRUE, 0);
    when(repository.update(task)            ).thenThrow(new OptimisticLockingFailureException(""));
    when(repository.selectById(task.getId())).thenReturn(task);
    // Exercise
    sut.edit(task);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void editはデータがない場合に例外をスローする() throws Exception {
    // SetUp
    Task task = new Task(1, "件名", "内容", Boolean.TRUE, 0);
    when(repository.update(task)            ).thenThrow(new OptimisticLockingFailureException(""));
    when(repository.selectById(task.getId())).thenReturn(null);
    // Exercise
    sut.edit(task);
  }

  @Test
  public void removeはデータがある場合は正常に終了する() throws Exception {
    // SetUp
    when(repository.delete(1)).thenReturn(1);
    // Exercise
    sut.remove(1);
    // Verify
    verify(repository).delete(1);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void removeはデータがない場合に例外をスローする() throws Exception {
    // SetUp
    when(repository.delete(1)).thenReturn(0);
    // Exercise
    sut.remove(1);
  }


}
