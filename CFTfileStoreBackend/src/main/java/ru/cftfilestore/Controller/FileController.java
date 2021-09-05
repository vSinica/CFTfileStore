package ru.cftfilestore.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
public class FileController {

    private String uploadPath;

    @PostConstruct
    private void postConstruct(){
        uploadPath = System.getProperty("catalina.base");
    }

    @PostMapping(path = "/addFile")
    public ResponseEntity<?> addFile(@RequestParam("file") MultipartFile file)  {
        String fileName = file.getOriginalFilename();

        try {
            file.transferTo(new File(uploadPath + "\\" + "tmp" + "\\" + fileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        System.out.println("Запись в хранилище прошла успешно");
        return ResponseEntity.ok("file uploaded");
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String filename) throws FileNotFoundException {
        String fullfilename = uploadPath+ "\\"+"tmp"+"\\" + filename;
        File file = new File(fullfilename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<InputStreamResource>
                responseEntity = ResponseEntity.ok().headers(headers).contentLength(
                file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);

        return responseEntity;
    }
}
