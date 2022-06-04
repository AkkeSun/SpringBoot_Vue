package com.example.springboot_vue.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloud_front.file_url_format}")
    private String cloud_front_url;

    private final AmazonS3Client amazonS3Client;


    // =================== 파일 업로드 =================
    public String upload(MultipartFile mFile, String dirName) throws IOException {

        File uploadFile = convert(mFile)
                .orElseThrow(()->new IllegalArgumentException("MultipartFile 형식을 File 로 전환하는 데에 실패하였습니다."));
        String uploadUrl = putS3(mFile.getOriginalFilename(), dirName, uploadFile);
        removeLocalFile(uploadFile);

        return uploadUrl;
    }


    // ================ 파일 삭제 ===================
    public void deleteS3File(String fName, String dirName){
        String fileName = getDirAndFileName(fName, dirName);
        try{
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            log.info("파일이 삭제되었습니다");
        } catch (Exception e) {
            log.info("파일 삭제에 실패하였습니다");
            log.info(e);
        }
    }


    // ================ 파일 다운로드 ===================
    public void fileDownload (String fName, String dirName, HttpServletResponse response) throws IOException {

        String dirAndFileName = getDirAndFileName(fName, dirName);
        S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucket, dirAndFileName));
        S3ObjectInputStream objectInputStream = object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fName, "UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }



    //=============================================================================================================
    // MultipartFile -> file 로 컨버팅하기 위한 함수
    private Optional<File> convert(MultipartFile file) throws IOException {

        // 이미 존재하는 파일의 경우 false 를 리턴하므로 System.currentTimeMillis() 를 추가
        File convertFile = new File(System.currentTimeMillis()+ file.getOriginalFilename());

        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


    // 하위 디렉토리가 있다면 파일명에 디렉토리명을 붙여주는 함수
    private String getDirAndFileName(String fileName, String dirName) {
        if(dirName == null || dirName.equals(""))
            return fileName;
        else
            return dirName + "/" + fileName;
    }


    // S3에 파일을 업로드하는 함수
    private String putS3(String fileName, String dirName, File uploadFile){

        String dirAndFileName = getDirAndFileName(fileName, dirName);

        try{
            amazonS3Client.putObject(new PutObjectRequest(bucket, dirAndFileName, uploadFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("S3 업로드에 성공했습니다");
        } catch (Exception e) {
            log.info("S3 업로드에 실패했습니다.");
            log.info(e);
        }

        return cloud_front_url + "/" + dirAndFileName;
    }


    // 생성한 로컬 파일을 삭제하는 함수
    private void removeLocalFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("로컬 파일이 삭제되었습니다.");
        } else {
            log.info("로컬 파일이 삭제되지 못했습니다.");
        }
    }

}
