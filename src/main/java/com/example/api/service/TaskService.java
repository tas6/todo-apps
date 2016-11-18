package com.example.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.api.entity.Task;
import com.example.api.exception.InvalidVersionException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.TaskRepository;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  public List<Task> selectAll() {
    return taskRepository.selectAll();
  }

  public Task selectById(int id) throws ResourceNotFoundException {
    Task task = taskRepository.selectById(id);
    if (task == null) {
      throw new ResourceNotFoundException();
    }
    return task;
  }

  public Task add(Task task) {
    return taskRepository.insert(task).getEntity();
  }

  public Task edit(Task task) throws ResourceNotFoundException, InvalidVersionException {
    try {
      return taskRepository.update(task).getEntity();
    } catch (OptimisticLockingFailureException ex) {
      if (taskRepository.selectById(task.getId()) != null) {
        throw new InvalidVersionException();
      } else {
        throw new ResourceNotFoundException();
      }
    }
  }

  public void remove(int id) throws ResourceNotFoundException {
    if (taskRepository.delete(id) == 0) {
      throw new ResourceNotFoundException();
    }
  }

}
