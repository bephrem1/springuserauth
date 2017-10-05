package com.teamtreehouse.todotoday.dao;

import com.teamtreehouse.todotoday.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //This is marked @Repository because it extends CrudRepository and DataConfig has
            //@EnableJpaRepositories(basePackages = "com.teamtreehouse.todotoday.dao") to tell Spring where DAO's are
            //The Implementations are therefore automatically generated for us upon running. Other methods we want can
            //be added here and implemented ourselves if we want
public interface TaskDao extends CrudRepository<Task, Long> { // <class you are after, data type of id> //
    @Query("select t from Task t where t.user.id=:#{principal.id}") //To use the principal we need to go to SecurityConfig
    List<Task> findAll();                                           //and add an Evaluation Context Extension

}


//JPQL - Java Persistence Query Language (We use it in the @Query annotation so we don't need to make an Impl class ourselves
//SpEL - Spring Expression Language