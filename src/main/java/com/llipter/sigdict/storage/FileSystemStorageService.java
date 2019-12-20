package com.llipter.sigdict.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemStorageService implements StorageService {

    private static final Path dirLocation = Paths.get("upload-dir");

    @Override
    public void store(MultipartFile file) {

    }

}
