package com.sandbox.filestorage.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.InputStream;

@Data
public class FileDto {

    @JsonIgnore
    private Long id;

    private String name;

    private Long version;

    @JsonIgnore
    private InputStream fileInputStream;

    private String token;


}
