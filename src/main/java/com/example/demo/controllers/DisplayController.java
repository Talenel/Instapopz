package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.Image;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 7/13/17.
 */
@Controller
@RequestMapping("/display")
public class DisplayController {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;

    @RequestMapping("/myphotos")
    public String viewMyPhotos(Model model, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        model.addAttribute("imageList",imageRepository.findAllByUserId(user.getId()));


        return "results";
    }
    @RequestMapping("/recentphotos")
    public String viewRecentPhotos(Model model, Principal principal)
    {
        List<Image> imageList2=imageRepository.findAll();
        ArrayList<Image> imageList=new ArrayList<>();
        for(int i=1;i<=10;i++)
        {
            if((imageList2.size()-i)>=0) {
                imageList.add(imageList2.get(imageList2.size() - i));
            }
        }
        model.addAttribute("imageList",imageList);


        return "results";
    }
    @RequestMapping("/morephotos/{id}")
    public String viewPhotos(@PathVariable("id") long id,Model model)
    {
        User user=userRepository.findOne(id);
        model.addAttribute("imageList",imageRepository.findAllByUserId(user.getId()));


        return "results";
    }
    @RequestMapping("/photo/{id}")
    public String viewPhoto(@PathVariable("id") long id, Model model, Principal principal)
    {
        Image image=imageRepository.findOne(id);
        model.addAttribute("image",image);
        User user=userRepository.findOne(image.getUserId());
        model.addAttribute("user",user);
        model.addAttribute("commentList",commentRepository.findAllByImageId(image.getId()));
        Comment comment=new Comment();
        comment.setImageId(image.getId());
        model.addAttribute("comment",comment);


        return "photo";
    }
    @RequestMapping("/profile")
    public String viewMyProfile( Model model, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("imageList",imageRepository.findAllByUserId(user.getId()));
        Boolean like=false;
        Boolean follow=false;
        model.addAttribute("like",like);
        model.addAttribute("follow", follow);

        return "profile";
    }
    @RequestMapping("/profile/{id}")
    public String viewProfile(@PathVariable("id") long id, Model model, Principal principal)
    {
        User user=userRepository.findOne(id);
        model.addAttribute("imageList",imageRepository.findAllByUserId(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("followedList",user.getFollowed());
        model.addAttribute("followerList",user.getFollower());

        return "profile";
    }
    @PostMapping("/comment")
    public String postComment(@ModelAttribute Comment comment, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        commentRepository.save(comment);

        return "redirect:/display/photo/"+comment.getImageId();
    }


}
