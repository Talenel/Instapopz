package com.example.demo.models;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by student on 7/11/17.
 */
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userId;

    private String imgSrc;

    private String caption;

    private String imgName;

    private int likeCount;
    
    private int filterValue;
    
    private String filterUrl1;

    private String filterUrl2;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="likes",joinColumns = @JoinColumn(name = "image_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Collection<User> likes;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Collection<User> getLikes() {
        return likes;
    }

    public void setLikes(Collection<User> likes) {
        this.likes = likes;
    }

    public int getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(int filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterUrl1() {
        return filterUrl1;
    }

    public void setFilterUrl1(String filterUrl1) {
        this.filterUrl1 = filterUrl1;
    }

    public String getFilterUrl2() {
        return filterUrl2;
    }

    public void setFilterUrl2(String filterUrl2) {
        this.filterUrl2 = filterUrl2;
    }
}
