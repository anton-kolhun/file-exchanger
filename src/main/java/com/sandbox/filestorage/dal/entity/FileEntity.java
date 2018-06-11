package com.sandbox.filestorage.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILE")
@Getter
@Setter
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Version
    private Long version;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<FileAccessEntity> fileAccesses = new ArrayList<>();


    public void setFileAccesses(List<FileAccessEntity> fileAccesses) {
        this.fileAccesses.clear();
        this.fileAccesses.addAll(fileAccesses);
    }

}
