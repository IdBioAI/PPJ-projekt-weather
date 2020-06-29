package com.weather.weather.rest;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.weather.weather.configurations.FileStorageProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.services.MongoDBService;
import com.weather.weather.services.MySQLService;
import com.weather.weather.services.OpenWeatherService;
import com.weather.weather.view.MainPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

@RestController
public class WebControl {

    @Autowired
    MainPage mainPage;
    @Autowired
    FileStorageProperties fileStorageProperties;
    @Autowired
    MySQLService mySQLService;
    @Autowired
    MongoDBService mongoDBService;
    @Autowired
    OpenWeatherService openWeatherService;

    Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String getStateInfo() {
        try {
            return mainPage.showMainPage(1);
        } catch (Exception e) {
            log.error("Mapping error /", e);
        }
        return "Error";
    }

    @GetMapping("/week")
    public String getWeekInfo() {
        try {
            return mainPage.showMainPage(7);
        } catch (Exception e) {
            log.error("Mapping error /week", e);
            return "Error " + e.getMessage();
        }
    }

    @GetMapping("/week2")
    public String getWeek2Info() {
        try {
            return mainPage.showMainPage(14);
        } catch (Exception e) {
            log.error("Mapping error /week2", e);
            return "Error " + e.getMessage();
        }
    }

    @PostMapping("/city/import")
    public void uploadFile(HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

            CsvToBean<CityMg> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CityMg.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<CityMg> csvUserIterator = csvToBean.iterator();

            while (csvUserIterator.hasNext()) {
                mongoDBService.saveData(csvUserIterator.next());
            }

            response.sendRedirect("/");

        } catch (IOException e) {
            log.error("Mapping error /city/import", e);
        }
    }

    @RequestMapping("/city/export/{filename:.+}")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable String filename) {

        String csvFile = filename + ".csv";

        try {

            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + csvFile + "\"");

            //export dat z databáze
            // csv writer
            StatefulBeanToCsv<CityMg> writer = new StatefulBeanToCsvBuilder<CityMg>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();
            writer.write(mongoDBService.selectValuesByName(filename));

        }catch (Exception ex){
            log.error("Mapping error /city/export", ex);
        }
    }

}
