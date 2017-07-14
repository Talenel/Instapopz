package com.example.demo.repositories;

import com.example.demo.models.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 7/10/17.
 */
public interface ImageRepository extends CrudRepository<Image,Long> {

    public List<Image> findAllByUserId(long userId);
    public List<Image> findTop12ByUserIdOrderByIdDesc(long userId);


    public List<Image> findAll();

}
