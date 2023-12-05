package com.example.backend.conrollers;

import com.example.backend.model.UserFile;
import com.example.backend.services.props.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FilesController {

    private final FileService fileService;

    @PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(@RequestParam String userId, @RequestParam Boolean isProfilePhoto,
                                        @RequestBody MultipartFile multipartFile) throws IOException {
        String uploadImage = fileService.uploadImage(multipartFile, userId, isProfilePhoto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }


    @GetMapping("/get-file")
    public ResponseEntity<?> getFile(
            @RequestParam String userId,
            @RequestParam Boolean isProfilePhoto) {
        UserFile imageData = fileService.downloadImage(userId);

        MediaType mediaType;
        switch (imageData.getExtension()) {
            case "image/png" -> mediaType = MediaType.IMAGE_PNG;
            case "image/jpg", "image/jpeg" -> mediaType = MediaType.IMAGE_JPEG;
            case "image/webp" -> mediaType = MediaType.parseMediaType("image/webp");
            default -> mediaType = MediaType.ALL;
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(mediaType)
                .body(imageData.getFileData());
    }

}
