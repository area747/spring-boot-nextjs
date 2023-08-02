package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.FileDTO;
import com.app.entity.FileEntity;
import com.app.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileService {

    private final String path = "F:/upload/";
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public <T> T convertToType(Object source, Class<T> resultClass) {
        return modelMapper.map(source, resultClass);
    }

    public <V,T> List<T> convertListToType(List<V> source, Class<T> resultClass) {
        Type listType = new TypeToken<List<T>>(){}.getType();

        return modelMapper.map(source, listType);
    }

    @Transactional(rollbackOn = {Exception.class})
    public void saveFiles(List<MultipartFile> fileList) throws IllegalStateException, IOException {
        for(int i = 0; i < fileList.size(); i++) {
            MultipartFile file = fileList.get(i);
            String saveFolder = path + file.getOriginalFilename();
            File folder = new File(saveFolder);
            FileDTO fileDTO = FileDTO.builder().fileName(file.getOriginalFilename()).path(saveFolder).build();
            FileEntity fileEntity = convertToType(fileDTO, FileEntity.class);
            file.transferTo(folder);
            if (i == 1) {
                throw new IOException();
            }
            fileRepository.save(fileEntity);
        }
    }

    public List<FileDTO> getFiles() {
        List<FileDTO> result = convertListToType(fileRepository.findAll(), FileDTO.class);

        return result;
    }

    @Transactional
    public void removeFiles(FileDTO fileDto) {
        FileEntity fileEntity = convertToType(fileDto, FileEntity.class);
        File folder = new File(fileEntity.getPath());
        fileRepository.delete(fileEntity);
        folder.delete();
    }

}
