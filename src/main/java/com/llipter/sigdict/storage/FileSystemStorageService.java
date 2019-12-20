package com.llipter.sigdict.storage;

import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.security.SignatureType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    private static final Path dirLocation = Paths.get("upload-dir");


    @Override
    public void store(MultipartFile file, User user, SignatureType signatureType, boolean encrypted) {
//        try {
//            InputStream inputStream = file.getInputStream();
//            Files.copy(inputStream, this.dirLocation.resolve(filename),
//                    StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
