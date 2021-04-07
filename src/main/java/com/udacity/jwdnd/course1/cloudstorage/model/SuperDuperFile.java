package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuperDuperFile {

    private Integer fileId;
    private String fileName;
    private String contentType;
    private String fileSize;
    private Integer userId;
    private byte [] fileData;

}
