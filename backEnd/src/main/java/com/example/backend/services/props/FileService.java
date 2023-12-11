package com.example.backend.services.props;

import com.example.backend.model.UserImageFile;
import com.example.backend.model.User;
import com.example.backend.repositories.UserFileRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FileService {

    private final UserFileRepository userFileRepository;
    private final UserRepository userRepository;

    @Transactional
    public String uploadImage(MultipartFile file, String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        userFileRepository.save(UserImageFile.builder()
                .fileName(file.getOriginalFilename())
                .extension(file.getContentType())
                .user(user)
                .fileData(FileUtils.compressImage(file.getBytes()))
                .createDate(new Date()).build());
        return "file uploaded successfully : " + file.getOriginalFilename();
    }

    @Transactional
    public UserImageFile downloadImage(String userId){
        User user = userRepository.findById(userId).orElseThrow();
        UserImageFile userImageFile = userFileRepository.findAllByUserOrderByCreateDateDesc(user);

        userImageFile.setFileData(FileUtils.decompressImage(userImageFile.getFileData()));
        return userImageFile;

    }
}
