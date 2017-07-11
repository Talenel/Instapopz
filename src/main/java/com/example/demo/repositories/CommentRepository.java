package com.example.demo.repositories;

import com.example.demo.models.Comment;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by student on 7/11/17.
 */
public interface CommentRepository extends CrudRepository<Comment,Long> {

}
