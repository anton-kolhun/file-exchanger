package com.sandbox.filestorage.service;

import com.sandbox.filestorage.dal.entity.FileAccessEntity;
import com.sandbox.filestorage.dal.entity.FileEntity;
import com.sandbox.filestorage.dal.repository.FileRepository;
import com.sandbox.filestorage.exception.FileAccessForbiddenException;
import com.sandbox.filestorage.exception.ValidationException;
import com.sandbox.filestorage.helper.FileHelper;
import com.sandbox.filestorage.service.dto.FileDto;
import com.sandbox.filestorage.service.dto.FileSearchCriteria;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileHelper fileHelper;


    @Transactional
    public FileDto saveFile(FileDto fileDto) throws IOException {
        FileEntity fileEntity = initFileEntity(fileDto);
        FileEntity createdFileEntity = fileRepository.saveAndFlush(fileEntity);
        FileDto createdFileDto = convertToDto(createdFileEntity);
        String filePath = fileHelper.resolveFilePathForSaving(createdFileDto);
        File outputFile = new File(filePath);
        FileUtils.copyInputStreamToFile(fileDto.getFileInputStream(), outputFile);
        return createdFileDto;
    }


    public FileDto find(FileSearchCriteria fileSearchCriteria) {
        if (StringUtils.isEmpty(fileSearchCriteria.getToken())) {
            throw new ValidationException("Http header X-Token should be provided");
        }
        List<FileEntity> fileEntities = fileRepository.findByName(fileSearchCriteria.getName());
        FileEntity matchingFile = filterByToken(fileSearchCriteria.getToken(), fileEntities);
        FileDto fileDto = convertToDto(matchingFile);
        if (fileSearchCriteria.getVersion() != null) {
            fileDto.setVersion(fileSearchCriteria.getVersion());
        }
        String fileName = fileHelper.resolveFilePathForSaving(fileDto);
        File file = new File(fileName);
        try {
            fileDto.setFileInputStream(new FileInputStream(file));
            return fileDto;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("requested file  does not exist", e);
        }

    }


    public static FileDto convertToDto(FileEntity matchingFile) {
        FileDto fileDto = new FileDto();
        fileDto.setName(matchingFile.getName());
        fileDto.setId(matchingFile.getId());
        fileDto.setVersion(matchingFile.getVersion());
        fileDto.setToken(matchingFile.getFileAccesses().get(0).getToken());
        return fileDto;
    }

    public static FileEntity convertToEntity(FileDto fileDto) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileDto.getName());
        return fileEntity;
    }

    private FileEntity initFileEntity(FileDto fileDto) {
        FileEntity fileEntity;
        if (fileDto.getToken() != null) {
            List<FileEntity> fileEntities = fileRepository.findByName(fileDto.getName());
            if (fileEntities.isEmpty()) {
                throw new EntityNotFoundException("file with name = " + fileDto.getName() + " does not exist");
            }
            fileEntity = filterByToken(fileDto.getToken(), fileEntities);
        } else {
            fileEntity = convertToEntity(fileDto);
            String token = RandomStringUtils.randomAlphabetic(8);
            FileAccessEntity fileAccessEntity = new FileAccessEntity();
            fileAccessEntity.setToken(token);
            fileAccessEntity.setFile(fileEntity);
            fileEntity.getFileAccesses().add(fileAccessEntity);
        }
        fileEntity.setModifiedAt(Calendar.getInstance().getTime());
        return fileEntity;
    }

    private FileEntity filterByToken(String token, List<FileEntity> fileEntities) {
        FileEntity fileEntity;
        fileEntity = fileEntities.stream()
                .filter(file -> file.getFileAccesses().stream()
                        .map(FileAccessEntity::getToken)
                        .collect(Collectors.toSet())
                        .contains(token))
                .findFirst()
                .orElseThrow(() -> new FileAccessForbiddenException("given token = " + token + " is not valid"));
        return fileEntity;
    }
}
