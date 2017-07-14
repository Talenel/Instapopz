package com.example.demo.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.example.demo.configs.CloudinaryConfig;
import com.example.demo.models.Image;
import com.example.demo.models.User;
import com.example.demo.repositories.ImageRepository;

import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.validators.UserValidator;
/*import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;*/
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import javax.mail.internet.InternetAddress;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by student on 7/10/17.
 */
@Controller
public class HomeController {

    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;


    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String home(Model model){

        return "home";
    }



    @RequestMapping("/login")
    public String login(){
        return "login";

    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user", user);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "register";
        }
        if(user.getRoleName().equals("user"))
        {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        if(user.getRoleName().equals("admin"))
        {
            userService.saveAdmin(user);
            model.addAttribute("message", "Admin Account Successfully Created");
        }
        return "home";
    }
    @RequestMapping("/like/{id}")
    public String likePhoto(@PathVariable("id") long id, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        Image image=imageRepository.findOne(id);
        if(image.getLikes().isEmpty()) {
            image.getLikes().add(user);
            image.setLikeCount(image.getLikeCount()+1);

        }
        else
        {
            image.getLikes().add(user);
            image.setLikeCount(image.getLikeCount()+1);
        }
        imageRepository.save(image);
        return "redirect:/display/photo/"+image.getId();
    }
    @RequestMapping("/follow/{id}")
    public String followUser(@PathVariable("id") long id, Principal principal)
    {
        User user=userRepository.findByUsername(principal.getName());
        User followed=userRepository.findOne(id);


            user.getFollowed().add(followed);


        userRepository.save(user);
        return "redirect:/display/profile/"+followed.getId();
    }



    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

/*

    @Autowired
    public EmailService emailService;
    public void sendEmailWithoutTemplating(User user, Meme meme){
        final Email email;
        try {
            email = DefaultEmail.builder()
                    .from(new InternetAddress("bot.orion.bot@gmail.com", "The MemeLord"))
                    .to(Lists.newArrayList(new InternetAddress(user.getEmail(),user.getUsername())))
                    .subject("Your Meme, Your Way")
                    .body("You have created a new meme. Here is the link: memez-memez.herokuapp.com/showmemes/"+meme.getId())
                    .encoding("UTF-8").build();
            emailService.send(email);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    */
}
