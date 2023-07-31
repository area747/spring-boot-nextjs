package com.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.app.dataMapper.FileRepository;
import com.app.entity.FileEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final FileRepository fileRepository;

    @GetMapping
    public String index() {
        return "Hello, Spring!";
    }

    @GetMapping
    @RequestMapping("/test")
    public String test() {
        return "success";
    }

    @RequestMapping(value="/data",method=RequestMethod.POST)
    public String data(MultipartHttpServletRequest request) throws IllegalStateException, IOException {
        String path = "F:/upload/";
        List<MultipartFile> files = request.getFiles("files");


        for (MultipartFile file : files) {
            System.out.println("fileName ::: "+ file.getOriginalFilename());
            String saveFolder = path + file.getOriginalFilename();
            File folder = new File(saveFolder);
            FileEntity fileEntity = FileEntity.builder().fileName(file.getOriginalFilename()).path(saveFolder).build();
            fileRepository.save(fileEntity);
            file.transferTo(folder);
        }

        return "success";
    }

    @RequestMapping(value="/data",method=RequestMethod.GET)
    public String getData(MultipartHttpServletRequest request) throws IllegalStateException, IOException {
        return "hi";
    }
}
