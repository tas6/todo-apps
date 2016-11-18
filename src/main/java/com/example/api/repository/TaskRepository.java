package com.example.api.repository;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.entity.Task;

@ConfigAutowireable
@Dao
@Transactional
public interface TaskRepository {

  @Select
  List<Task> selectAll();

  @Select
  Task selectById(Integer id);

  @Insert
  Result<Task> insert(Task task);

  @Update
  Result<Task> update(Task task);

  @Delete(sqlFile = true)
  int delete(Integer id);

}
