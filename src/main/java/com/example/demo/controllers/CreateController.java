package com.example.demo.controllers;

import com.cloudinary.utils.ObjectUtils;
import com.example.demo.configs.CloudinaryConfig;
import com.example.demo.models.Image;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * Created by student on 7/12/17.
 */
@Controller
@RequestMapping("/create")
public class CreateController {

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/upload")
    public String uploadForm(Model model){
        model.addAttribute("image", new Image());
        return "upload";
    }
    @PostMapping("/upload")
    public String singleImageUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @ModelAttribute Image image, Model model){
        if (file.isEmpty()){
            model.addAttribute("message","Please select a file to upload");
            return "upload";
        }
        try {
            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            model.addAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
            model.addAttribute("imageurl", uploadResult.get("url"));
            String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
            //model.addAttribute("sizedimageurl", cloudc.createSizedUrl(filename,150,200, "limit"));
            image.setImgName(filename);
            image.setImgSrc(uploadResult.get("url").toString());
            image.setFilterValue(0);
            image=checkFilter(image);
            //image.setImgSrc((String)  cloudc.createSizedUrl(filename,150,200, "limit"));
            //imageRepository.save(image);
            model.addAttribute("image", image);
        } catch (IOException e){
            e.printStackTrace();
            model.addAttribute("message", "Sorry I can't upload that!");
        }
        return "preview";

    }

    @RequestMapping("/preview")
    public String previewImage(@ModelAttribute Image image, Model model)
    {
        image=checkFilter(image);
        model.addAttribute("image", image);





        return "preview";
    }
    @RequestMapping("/save")
    public String saveImage(@ModelAttribute Image image, Principal principal)
    {
        image.setLikeCount(0);
        image.setUserId(userRepository.findByUsername(principal.getName()).getId());

        imageRepository.save(image);

        return "home";
    }



    private Image checkFilter(Image image)
    {
        switch(image.getFilterValue()){
            case 1: image.setFilterUrl2((String) cloudc.createColorImageSize(image.getImgName(),"blue",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createColorImageSize(image.getImgName(),"blue",500,500,"limit"));
                return image;
            case 2: image.setFilterUrl2((String) cloudc.createColorImageSize(image.getImgName(),"yellow",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createColorImageSize(image.getImgName(),"yellow",500,500,"limit"));
                return image;
            case 3: image.setFilterUrl2((String) cloudc.createColorImageSize(image.getImgName(),"green",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createColorImageSize(image.getImgName(),"green",500,500,"limit"));
                return image;
            case 4: image.setFilterUrl2((String) cloudc.createColorImageSize(image.getImgName(),"red",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createColorImageSize(image.getImgName(),"red",500,500,"limit"));
                return image;
            case 5: image.setFilterUrl2((String) cloudc.createColorImageSize(image.getImgName(),"purple",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createColorImage(image.getImgName(),"purple",500,500,"limit"));
                return image;
            case 6: image.setFilterUrl2((String) cloudc.createSepiaImageSize(image.getImgName(),":20",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createSepiaImageSize(image.getImgName(),":20",500,500,"limit"));
                return image;
            case 7: image.setFilterUrl2((String) cloudc.createLightFillImageSize(image.getImgName(),":20",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createLightFillImageSize(image.getImgName(),":20",500,500,"limit"));
                return image;
            case 8: image.setFilterUrl2((String) cloudc.changeBrightnessImageSize(image.getImgName(),":50",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.changeBrightnessImageSize(image.getImgName(),":50",500,500,"limit"));
                return image;
            case 9: image.setFilterUrl2((String) cloudc.changeBrightnessImageSize(image.getImgName(),":-50",150,200,"limit"));
                image.setFilterUrl1((String) cloudc.changeBrightnessImageSize(image.getImgName(),":-50",500,500,"limit"));
                return image;
            case 10: image.setFilterUrl2((String) cloudc.autoBrightnessImageSize(image.getImgName(),150,200,"limit"));
                image.setFilterUrl1((String) cloudc.autoBrightnessImageSize(image.getImgName(),500,500,"limit"));
                return image;
            default:image.setFilterUrl2((String) cloudc.createSizedUrl(image.getImgName(),150,200,"limit"));
                image.setFilterUrl1((String) cloudc.createSizedUrl(image.getImgName(),500,500,"limit"));
                return image;
        }


    }


}
