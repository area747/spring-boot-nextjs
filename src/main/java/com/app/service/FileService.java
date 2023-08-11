package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.app.dto.FileDTO;
import com.app.entity.FileEntity;
import com.app.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileService {

    private final String path = "D:/upload/";
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public <T> T convertToType(Object source, Class<T> resultClass) {
        return modelMapper.map(source, resultClass);
    }

    public <V, T> List<T> convertListToType(final List<V> source, Class<T> resultClass) {
        return source.stream().map(entity -> {
            return convertToType(entity, resultClass);
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackOn = { Exception.class })
    public void saveFiles(List<MultipartFile> fileList) throws IllegalStateException, IOException {
        for (int i = 0; i < fileList.size(); i++) {
            MultipartFile file = fileList.get(i);
            String uuid = UUID.randomUUID().toString();
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            File folder = new File(path + uuid + "." + extension);
            FileDTO fileDTO = FileDTO.builder().id(uuid).fileName(file.getOriginalFilename()).extension(extension).build();
            FileEntity fileEntity = convertToType(fileDTO, FileEntity.class);
            file.transferTo(folder);
            fileRepository.save(fileEntity);
        }
    }

    public List<FileDTO> getFiles() {
        List<FileDTO> result = convertListToType(fileRepository.findAllByOrderByInDateAsc().get(), FileDTO.class);

        return result;
    }

    public byte[] downloadFile(String uuid) throws IOException {
        FileEntity fileEntity = fileRepository.findById(uuid).get();
        FileDTO file = convertToType(fileEntity, FileDTO.class);
        byte[] res = Files.readAllBytes(Paths.get(path + file.getId() + "." + file.getExtension()).normalize());

        return res;
    }

    @Transactional(rollbackOn = { Exception.class })
    public void removeFiles(FileDTO fileDto) {
        File folder = new File(path + fileDto.getId() + "." + fileDto.getExtension());
        FileEntity fileEntity = convertToType(fileDto, FileEntity.class);
        fileRepository.delete(fileEntity);
        folder.delete();
    }

}
