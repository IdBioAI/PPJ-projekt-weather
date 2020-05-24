package com.weather.weather.rest;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.weather.weather.Main;
import com.weather.weather.configurations.ConfigProperties;
import com.weather.weather.configurations.FileStorageProperties;
import com.weather.weather.model.CityMg;
import com.weather.weather.view.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

@RestController
public class WebControl {

    @Autowired
    MainPage mainPage;
    ConfigProperties configProperties;
    @Autowired
    FileStorageProperties fileStorageProperties;

    @GetMapping("/")
    public String GetStateInfo() {
        return mainPage.ShowMainPage(0);
    }

    @GetMapping("/week")
    public String getWeekInfo() {
        return mainPage.ShowMainPage(1);
    }

    @GetMapping("/week2")
    public String getWeek2Info() {
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

            // import do databáze
            Reader reader = Files.newBufferedReader(Paths.get(fileStorageProperties.getUploadDir() + file.getOriginalFilename()));
            CsvToBean<CityMg> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CityMg.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<CityMg> csvUserIterator = csvToBean.iterator();

            CityMg cityMg;
            while (csvUserIterator.hasNext()) {
                Main.getMongoDBService().SaveData(csvUserIterator.next());
            }

            response.sendRedirect("/");

        } catch (IOException e) {
            Main.getLog().error(e.getMessage());
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
            writer.write(Main.getMongoDBService().SelectValuesByName(filename));

        }catch (Exception ex){
            Main.getLog().error(ex.getMessage());
        }

        /*try {
            File file = new File(fileStorageProperties.getDownloadDir() + "/" + filename + ".csv");

            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());

        }catch (Exception ex){
            // TODO vypsání do logu
        }*/

    }

}
