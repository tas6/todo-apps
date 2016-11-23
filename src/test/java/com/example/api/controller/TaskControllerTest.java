package com.example.api.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.api.controller.request.RequestTask;
import com.example.api.entity.Task;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @InjectMocks
  TaskController taskController;

  @Mock
  TaskService taskService;

  @InjectMocks
  RestExceptionHandler handler;

  MockMvc mvc;

  @Autowired
  ObjectMapper jsonMapper;

  @Before
  public void setUp() throws Exception {
    this.mvc = MockMvcBuilders
        .standaloneSetup(taskController)
        .setControllerAdvice(handler)
        .build();
  }

  @Test
  public void getでタスクのリストが取得できる() throws Exception {
    // SetUp
    List<Task> tasks = new ArrayList<>();
    tasks.add(new Task(1, "件名", "内容", Boolean.FALSE, 1));
    when(taskService.selectAll()).thenReturn(tasks);
    // Exercise, Verify
    mvc
        .perform(get("/v1/tasks"))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonMapper.writeValueAsString(tasks)))
        .andDo(print());
  }

  @Test
  public void getでidパラメータを指定した場合にタスクが取得できる() throws Exception {
    // SetUp
    Task task = new Task(1, "件名", "内容", Boolean.FALSE, 1);
    when(taskService.selectById(1)).thenReturn(task);
    // Exercise, Verify
    mvc
        .perform(get("/v1/tasks/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(content().json(jsonMapper.writeValueAsString(task)))
        .andDo(print());
  }

  @Test
  public void getでidパラメータを指定した場合にデータが存在しない場合はNotFoundが返る() throws Exception {
    // SetUp
    when(taskService.selectById(1)).thenThrow(new ResourceNotFoundException());
    // Exercise, Verify
    mvc
        .perform(get("/v1/tasks/{id}", 1))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  public void postでタスクを登録できる() throws Exception {
    // SetUp
    Task task     = new Task(null, "件名", "内容", Boolean.TRUE, null);
    Task expected = new Task(1   , "件名", "内容", Boolean.TRUE, 1);
    when(taskService.add(task)).thenReturn(expected);
    // Exercise, Verify
    mvc
      .perform(post("/v1/tasks/")
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonMapper.writeValueAsString(task)))
      .andExpect(status().isCreated())
      .andExpect(content().json(jsonMapper.writeValueAsString(expected)))
      .andDo(print());
  }

  @Test
  public void putでタスクを更新できる() throws Exception {
    // SetUp
    RequestTask req      = new RequestTask("new件名", "new内容", Boolean.TRUE, 1);
    Task        task     = new Task(1, req);
    Task        expected = new Task(1, "new件名", "new内容", Boolean.TRUE, 2);
    when(taskService.edit(task)).thenReturn(expected);
    // Exercise, Verify
    mvc
      .perform(put("/v1/tasks/{id}", task.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(jsonMapper.writeValueAsString(req)))
      .andExpect(status().isOk())
      .andExpect(content().json(jsonMapper.writeValueAsString(expected)))
      .andDo(print());
  }

  @Test
  public void putで対象データがない場合はNotFoundが返る() throws Exception {
    // SetUp
    RequestTask req  = new RequestTask("件名", "内容", Boolean.TRUE, 1);
    Task        task = new Task(1, req);
    when(taskService.edit(task)).thenThrow(new ResourceNotFoundException());
    // Exercise, Verify
    mvc
        .perform(put("v1/tasks/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  public void deleteでタスクを削除できる() throws Exception {
    // SetUp
    doNothing().when(taskService).remove(1);
    // Exercise, Verify
    mvc
      .perform(delete("/v1/tasks/{id}", 1))
      .andExpect(status().isNoContent())
      .andDo(print());
  }

  @Test
  public void deleteで対象データがない場合はNotFoundが返る() throws Exception {
    // SetUp
    doThrow(new ResourceNotFoundException()).when(taskService).remove(1);
    // Exercise, Verify
    mvc
        .perform(put("v1/tasks/{id}", 1))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

}
