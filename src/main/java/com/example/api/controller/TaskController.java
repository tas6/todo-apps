package com.example.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.api.controller.request.RequestTask;
import com.example.api.entity.Task;
import com.example.api.exception.InvalidVersionException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.service.TaskService;

@RestController
@RequestMapping(path = "v1/tasks")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @RequestMapping(method = RequestMethod.GET)
  List<Task> selectAllTasks() {
    return taskService.selectAll();
  }

  @RequestMapping(method = RequestMethod.GET, path = "{id}")
  Task selectTask(@PathVariable Integer id) throws ResourceNotFoundException {
    return taskService.selectById(id);
  }

  @RequestMapping(method = RequestMethod.POST)
  ResponseEntity<Task> addTask(
      @RequestBody @Valid RequestTask requestTask,
      UriComponentsBuilder uriBuilder) {

    Task createdTask = taskService.add(Task.createTask(null, requestTask));

    return createResponseEntityCreated(
        createdTask,
        uriBuilder,
        "api/tasks/{id}",
        createdTask.getId());
  }

  @RequestMapping(method = RequestMethod.PUT, path = "{id}")
  Task editTask(
      @PathVariable Integer id,
      @RequestBody @Valid RequestTask requestTask)
      throws ResourceNotFoundException, InvalidVersionException {

    return taskService.edit(Task.createTask(id, requestTask));
  }

  @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  void removeTask(@PathVariable Integer id)
      throws ResourceNotFoundException, InvalidVersionException {

    taskService.remove(id);
  }

  private <T> ResponseEntity<T> createResponseEntityCreated(
      T task, UriComponentsBuilder uriBuilder, String path, Object key) {

    URI location = uriBuilder.path(path).buildAndExpand(key).toUri();
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(location);

    return new ResponseEntity<>(task, headers, HttpStatus.CREATED);

  }

}
