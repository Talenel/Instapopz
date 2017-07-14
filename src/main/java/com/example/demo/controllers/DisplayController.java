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
    @RequestMapping("/myfeed")
    public String viewMyFeed(Model model, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        ArrayList<Image> imageList=new ArrayList<>();
        for(User followed:user.getFollowed()) {
            imageList.addAll(imageRepository.findAllByUserId(followed.getId()));


        }
        if(!imageList.isEmpty()) {
            imageList = sortList(imageList);
        }

        model.addAttribute("imageList",imageList);


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
        User current=userRepository.findByUsername(principal.getName());
        model.addAttribute("current",current);
        model.addAttribute("like",checkLikes(image,current));



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
        model.addAttribute("current",user);
        model.addAttribute("followedList",user.getFollowed());
        model.addAttribute("followerList",user.getFollower());
        model.addAttribute("follow", follow);

        return "profile";
    }
    @RequestMapping("/profile/{id}")
    public String viewProfile(@PathVariable("id") long id, Model model, Principal principal)
    {
        User current=userRepository.findByUsername(principal.getName());
        User user=userRepository.findOne(id);
        model.addAttribute("imageList",imageRepository.findAllByUserId(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("current",current);
        model.addAttribute("followedList",user.getFollowed());
        model.addAttribute("followerList",user.getFollower());

        model.addAttribute("follow", checkFollows(user,current));

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

    private boolean checkLikes(Image image, User liker)
    {
        if(image.getUserId()==liker.getId())
        {
            return false;
        }

        for(User user:image.getLikes())
        {
            if(user.getId()==liker.getId())
            {
                return false;
            }
        }


        return true;
    }
    private boolean checkFollows(User followed, User pFollower)
    {
        if(followed.getId()==pFollower.getId())
        {
            return false;
        }

        for(User user:followed.getFollower())
        {
            if(user.getId()==pFollower.getId())
            {
                return false;
            }
        }


        return true;
    }



    private ArrayList<Image> sortList(ArrayList<Image> list)
    {
        ArrayList<Image> low = new ArrayList<Image>();
        ArrayList<Image> high= new ArrayList<Image>();
        ArrayList<Image> sorted= new ArrayList<Image>();
        int midIndex;

        if (list.size() == 1) {
            return list;
        }
        else
        {
            midIndex=list.size()/2;
            for (int i=0; i<midIndex; i++) {
                low.add(list.get(i));
            }

            //copy the high half of whole into the new arraylist.
            for (int i=midIndex; i<list.size(); i++) {
                high.add(list.get(i));
            }
            low=sortList(low);
            high=sortList(high);

            sorted=mergeList(low,high);

        }

        return sorted;
    }

    private ArrayList<Image> mergeList(ArrayList<Image> low,ArrayList<Image> high)
    {
        int lowIndex=0;
        int highIndex=0;
        int totalIndex=0;
        ArrayList<Image> sorted= new ArrayList<Image>();
        while (lowIndex < low.size() && highIndex < high.size()) {
            if (low.get(lowIndex).getId()>high.get(highIndex).getId()) {
                sorted.add(low.get(lowIndex));
                lowIndex++;
            } else {
                sorted.add(high.get(highIndex));
                highIndex++;
            }
        }
        ArrayList<Image> rest;
        int restIndex;
        if (lowIndex >= low.size()) {
            // The low ArrayList has been use up...
            rest = high;
            restIndex = highIndex;
        } else {
            // The high ArrayList has been used up...
            rest = low;
            restIndex = lowIndex;
        }

        // Copy the rest of whichever ArrayList (low or high) was not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            sorted.add(rest.get(i));
        }


        return sorted;

    }


}
    



