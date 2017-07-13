package com.example.demo.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryConfig {
    private Cloudinary cloudinary;
    @Autowired
    public CloudinaryConfig(@Value("${cloudinary.apikey}") String key,
                            @Value("${cloudinary.apisecret}") String secret,
                            @Value("${cloudinary.cloudname}") String cloud){
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName=cloud;
        cloudinary.config.apiSecret=secret;
        cloudinary.config.apiKey=key;
    }
    public Map upload(Object file, Map options){
        try{
            return cloudinary.uploader().upload(file, options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String createSizedUrl(String name, int width, int height, String action){
        return cloudinary.url()
                .transformation(new Transformation().width(width).height(height).border("2px_solid_black").crop(action))
                .imageTag(name);
    }
    
    public String createColorImageSize(String name, String color,int width, int height, String action)
    {
       return cloudinary.url()
               .transformation(new Transformation()
                       .effect("colorize:30").color(color).chain()
                       .width(width).height(height).border("2px_solid_black").crop(action))
               .imageTag(name);
    }
    public String createSepiaImageSize(String name,String value,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("sepia").chain()
                        .width(width).height(height).border("2px_solid_black").crop(action))
                .imageTag(name);
    }
    public String createLightFillImageSize(String name, String value,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("fill_light").chain()
                        .width(width).height(height).border("2px_solid_black").crop(action))
                .imageTag(name);
    }

    public String changeBrightnessImageSize(String name,String value,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("brightness"+value).chain()
                        .width(width).height(height).border("2px_solid_black").crop(action))
                .imageTag(name);
    }
    public String autoBrightnessImageSize(String name,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("auto_brightness").chain()
                        .width(width).height(height).border("2px_solid_black").crop(action))
                .imageTag(name);
    }
    public String createColorImage(String name, String color,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("colorize:30").color(color))
                .imageTag(name);
    }
    public String createSepiaImage(String name,String value,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("sepia"))
                .imageTag(name);
    }
    public String createLightFillImage(String name, String value,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("fill_light"))
                .imageTag(name);
    }

    public String changeBrightnessImage(String name,String value,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("brightness"+value))
                .imageTag(name);
    }
    public String autoBrightnessImage(String name,int width, int height, String action)
    {
        return cloudinary.url()
                .transformation(new Transformation()
                        .effect("auto_brightness"))
                .imageTag(name);
    }
}