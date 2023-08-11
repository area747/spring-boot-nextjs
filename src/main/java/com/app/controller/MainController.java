package com.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.app.dto.FileDTO;
import com.app.entity.FileEntity;
import com.app.repository.FileRepository;
import com.app.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final FileService fileService;

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
        List<MultipartFile> files = request.getFiles("files");

        fileService.saveFiles(files);

        return "success";
    }

    @RequestMapping(value="/data",method=RequestMethod.GET)
    public List<FileDTO> getData() throws IllegalStateException, IOException {
        List<FileDTO> fileList = fileService.getFiles();

        return fileList;
    }

    @RequestMapping(value="/file/{uuid}",method=RequestMethod.GET)
    public byte[] downloadFile(@PathVariable("uuid") String uuid) throws IllegalStateException, IOException {
        byte[] file = fileService.downloadFile(uuid);

        return file;
    }

    @RequestMapping(value="/data",method=RequestMethod.DELETE)
    public String removeData(@RequestBody @Valid FileDTO fileDto) throws IllegalStateException, IOException {
        fileService.removeFiles(fileDto);

        return "sueccess";
    }
}
