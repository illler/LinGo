package com.example.backend.services.props;

import com.example.backend.model.UserFile;
import com.example.backend.model.User;
import com.example.backend.repositories.FileRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Transactional
    public String uploadImage(MultipartFile file, String userId, boolean isProfile) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        fileRepository.save(UserFile.builder()
                .fileName(file.getOriginalFilename())
                .extension(file.getContentType())
                .isProfilePhoto(isProfile)
                .user(user)
                .fileData(FileUtils.compressImage(file.getBytes()))
                .createDate(new Date()).build());
        return "file uploaded successfully : " + file.getOriginalFilename();
    }

    @Transactional
    public UserFile downloadImage(String userId){
        User user = userRepository.findById(userId).orElseThrow();
        UserFile userFile = fileRepository.findAllByUserOrderByCreateDateDesc(user);

        userFile.setFileData(FileUtils.decompressImage(userFile.getFileData()));
        return userFile;

    }
}
