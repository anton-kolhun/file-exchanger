package com.sandbox.filestorage.controller;

import com.sandbox.filestorage.exception.ValidationException;
import com.sandbox.filestorage.service.FileService;
import com.sandbox.filestorage.service.dto.FileDto;
import com.sandbox.filestorage.service.dto.FileSearchCriteria;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("{fileName}")
    public FileDto getFileMetaData(@PathVariable String fileName,
                                   @RequestHeader(value = "X-Token") String fileToken) {

        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setName(fileName);
        fileSearchCriteria.setToken(fileToken);
        FileDto fileDto = fileService.find(fileSearchCriteria);
        return fileDto;
    }

    @GetMapping("{fileName}/body")
    public ResponseEntity<StreamingResponseBody> getFileBody(@RequestHeader(value = "Range", required = false) String rangeHeader,
                                                             @PathVariable String fileName,
                                                             @RequestHeader(value = "X-Token") String fileToken,
                                                             @RequestParam(required = false) Long version) {
        List<HttpRange> ranges = HttpRange.parseRanges(rangeHeader);
        HttpRange httpRange = extractRange(ranges);
        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setName(fileName);
        fileSearchCriteria.setToken(fileToken);
        fileSearchCriteria.setVersion(version);
        FileDto fileDto = fileService.find(fileSearchCriteria);
        StreamingResponseBody streamingResponseBody =
                outputStream -> IOUtils.copy(fileDto.getFileInputStream(), outputStream);

        ResponseEntity.BodyBuilder bodyBuilder;
        StringBuilder outputFileName = new StringBuilder(fileDto.getName());
        if (ranges.size() == 1) {
            bodyBuilder = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT);
            outputFileName.append("-bytes_range:")
                    .append(httpRange.getRangeStart(0))
                    .append("-")
                    .append(httpRange.getRangeEnd(Long.MAX_VALUE));
        } else {
            bodyBuilder = ResponseEntity.ok();
        }
        return bodyBuilder
                .header("Content-Disposition", "attachment; filename=" + outputFileName.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(streamingResponseBody);
    }


    @PostMapping
    public FileDto saveFile(@RequestParam MultipartFile file,
                            @RequestHeader(value = "X-Token", required = false) String fileToken) throws IOException {
        FileDto fileDto = new FileDto();
        fileDto.setName(file.getOriginalFilename());
        fileDto.setFileInputStream(file.getInputStream());
        fileDto.setToken(fileToken);
        FileDto createdFile = fileService.saveFile(fileDto);
        return createdFile;
    }

    private HttpRange extractRange(List<HttpRange> ranges) {
        if (ranges.size() > 1) {
            throw new ValidationException("at most one range is supported");
        }
        if (ranges.size() == 1) {
            return ranges.get(0);
        }
        return HttpRange.createByteRange(0, Long.MAX_VALUE);
    }
}
