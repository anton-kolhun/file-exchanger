package com.sandbox.filestorage.controller;

import com.sandbox.filestorage.service.dto.FileDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerTest {

    @LocalServerPort
    private String port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getFileMetaData() throws Exception {
        String testFileName = "upload-file.txt";
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", getUserFileResource(testFileName));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        //1. Save file
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/file",
                HttpMethod.POST, requestEntity, String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        //2. Read Metadata of saved file
        FileDto result = objectMapper.readValue(response.getBody(), FileDto.class);
        HttpHeaders headersForFileRetrieving = new HttpHeaders();
        headersForFileRetrieving.add("X-Token", result.getToken());
        HttpEntity<MultiValueMap<String, Object>> requestEntityForRetrieving = new HttpEntity<>(null, headersForFileRetrieving);
        ResponseEntity<FileDto> retrievedFileResponse = restTemplate.exchange("http://localhost:" + port +
                "/file/" + testFileName, HttpMethod.GET, requestEntityForRetrieving, FileDto.class);
        Assert.assertEquals(HttpStatus.OK, retrievedFileResponse.getStatusCode());

        //3. Download saved file
        ResponseEntity<FileDto> downloadFileResponse = restTemplate.exchange("http://localhost:" + port +
                "/file/" + testFileName + "/body", HttpMethod.GET, requestEntityForRetrieving, FileDto.class);
        Assert.assertEquals(HttpStatus.OK, downloadFileResponse.getStatusCode());
    }


    private org.springframework.core.io.Resource getUserFileResource(String fileName) throws IOException {
        File file = new File(fileName);
        return new FileSystemResource(file);
    }

}
