package com.weather.weather.rest;


import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.configurations.FileStorageProperties;
import com.weather.weather.view.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class RestControl {

    @Autowired
    MainPage mainPage;
    @Autowired
    ConfigProperties configProperties;
    @Autowired
    FileStorageProperties fileStorageProperties;

   @GetMapping("/")
    String GetStateInfo() {
        return mainPage.ShowMainPage(0);

    }
    @GetMapping("/week")
    String getWeekInfo() {
        return mainPage.ShowMainPage(1);
    }

    @GetMapping("/week2")
    String getWeek2Info() {
        return mainPage.ShowMainPage(2);
    }

    @PostMapping("/city/import")
    public void uploadFile(HttpServletResponse response, @RequestParam("file") MultipartFile file) {

       /* if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }*/

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileStorageProperties.getUploadDir() + file.getOriginalFilename());
            Files.write(path, bytes);

            /*redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");*/

            response.sendRedirect("/");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/city/export/{filename:.+}")
    public void downloadFile(@PathVariable String filename) {

       //export dat z databáze

        File file = new File(fileStorageProperties.getDownloadDir() + "/" + filename);


    }

    @PostMapping("/state/change")
    void UpdateState(HttpServletResponse response, @RequestParam("State")  String state) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMySQLService().ChangeState(state);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/city/add")
    void AddCity(HttpServletResponse response,@RequestParam("City")  String city) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMySQLService().AddCity(city);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/city/delete")
    void DeleteCity(HttpServletResponse response,@RequestParam("City")  String city) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMySQLService().DeleteCity(city);
        Main.getMongoDBService().deleteCities(city);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/temp/delete")
    void DeleteTemp(HttpServletResponse response,@RequestParam("City")  String city, @RequestParam("Date")  String date) {

        if(configProperties.isReadOnly()){ return; }

        Main.getMongoDBService().deleteDate(city, date);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
