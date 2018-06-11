package com.sandbox.filestorage.helper;

import com.sandbox.filestorage.service.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileHelper {

    @Value("${file.root.folder}")
    private String fileRootFolder;


    public String resolveFilePathForSaving(FileDto createdFileDto) {
        return new StringBuilder(fileRootFolder)
                .append("/id_")
                .append(createdFileDto.getId())
                .append("/version_")
                .append(createdFileDto.getVersion())
                .append("/")
                .append(createdFileDto.getName())
                .toString();
    }

}
