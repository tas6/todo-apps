package com.example.api.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

import com.example.api.controller.request.RequestTask;

import lombok.Data;

@Entity(immutable = true)
@Table(name = "tasks")
@Data
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  final Integer id;

  @NotNull
  @Length(min = 1, max = 100)
  final String subject;

  @NotNull
  @Length(min = 1, max = 500)
  final String description;

  final Boolean done;

  @Version
  final Integer version;

  public Task(
      Integer id,
      String subject,
      String description,
      Boolean done,
      Integer version) {

    this.id           = id;
    this.subject      = subject;
    this.description  = description;
    this.done         = done;
    this.version      = version;
  }

  public static Task createTask(Integer id, RequestTask request) {
    return new Task(
        id,
        request.getSubject(),
        request.getDescription(),
        request.getDone(),
        request.getVersion());
  }

}
