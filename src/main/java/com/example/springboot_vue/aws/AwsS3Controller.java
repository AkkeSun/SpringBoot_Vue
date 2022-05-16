package com.example.springboot_vue.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/aws")
public class AwsS3Controller {

    @Autowired
    private AwsS3Service s3Service;


    @PostMapping("/upload")
    public HashMap<String, String> fileUpload(MultipartFile file) throws IOException {

        String fileName = s3Service.upload(file, "instagram");

        HashMap<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return map;
    }


    @GetMapping("/download/{fileName}")
    public void fileDownload(@PathVariable (value = "fileName")String fileName, HttpServletResponse response) throws IOException {
        s3Service.fileDownload(fileName, "instagram", response);
    }


    @GetMapping("/delete/{fileName}")
    public void fileDelete(@PathVariable (value = "fileName")String fileName) {
        s3Service.deleteS3File(fileName, "instagram");
    }

}
