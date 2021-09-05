package ru.cftfilestore.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FileListController {

    private String uploadPath;

    @PostConstruct
    private void postConstruct(){
        uploadPath = System.getProperty("catalina.base");
    }

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("getfilelist")
    public String getFileList() throws JsonProcessingException {

        File folder = new File(uploadPath+ "\\"+"tmp\\");
        List<String> fileList = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                fileList.add(fileEntry.getName());
                System.out.println(fileEntry.getName());
            }
        }

        return objectMapper.writeValueAsString(fileList);
    }

}
