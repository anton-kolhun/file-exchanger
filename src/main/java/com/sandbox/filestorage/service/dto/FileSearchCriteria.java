package com.sandbox.filestorage.service.dto;

import lombok.Data;

@Data
public class FileSearchCriteria {

    private String name;

    private Long version;

    private String token;
}
