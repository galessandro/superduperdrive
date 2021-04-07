package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.SuperDuperFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid= #{userId}")
    List<SuperDuperFile> getFilesByUser(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid= #{fileId}")
    SuperDuperFile getFileById(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(SuperDuperFile sdFile);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int delete(Integer fileId);
}
