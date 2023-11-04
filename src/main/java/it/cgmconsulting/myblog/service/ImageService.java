package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.repository.ImageRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${application.contentImage.path}")
    private String imagePath;

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ContentService contentService;

    public ResponseEntity<?> checkImage(long size, int width, int height, String[] extensions, UserDetails userDetails, MultipartFile file){
        if(checkSize(file, size))
            return new ResponseEntity("File too large", HttpStatus.BAD_REQUEST);

        if(checkDimensions(width, height, file))
            return new ResponseEntity("Wrong file dimensions", HttpStatus.BAD_REQUEST);

        if(checkExtensions(file, extensions))
            return new ResponseEntity("File type non allowed", HttpStatus.BAD_REQUEST);

        return new ResponseEntity(true, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> addImageToContent(long size, int width, int height, String[] extensions, UserDetails userDetails, MultipartFile file, int contentId) throws IOException {
        Content co = contentService.getContentById(contentId);
        if(co.getImages().size() > 0 && co.getType() == 'H')
            return new ResponseEntity("The content has already an image (content type H)", HttpStatus.BAD_REQUEST);
        if(checkImage(size, width, height, extensions, userDetails, file).getStatusCode().equals(HttpStatus.OK)){
            String filename = file.getOriginalFilename();
            String ext = filename.substring(filename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString()+ext;
            Path path = Paths.get(imagePath+newFilename); // path = "/Users/Public/myblog/images/pippo.jpg".
            Files.write(path, file.getBytes());
            Image i = new Image(newFilename);
            imageRepository.save(i);
            co.getImages().add(i);
            return new ResponseEntity("Image added to content", HttpStatus.CREATED);
        }
        return new ResponseEntity("Something went wrong adding image to content", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> removeImageFromContent(int imageId) throws IOException {
        Image i = geImageById(imageId);
        String filename = i.getImage();
        imageRepository.deleteImageFromContent(imageId);
        imageRepository.delete(i);
        Path path = Paths.get(imagePath+filename); // path = "/Users/Public/myblog/images/pippo.jpg".
        Files.delete(path);
        return new ResponseEntity("Image has been removed from content", HttpStatus.OK);
    }

    public ResponseEntity<?> addAvatar(long size, int width, int height, String[] extensions, UserDetails userDetails, MultipartFile file) throws IOException {
        if(checkImage(size, width, height, extensions, userDetails, file).getStatusCode().equals(HttpStatus.OK)){
            Avatar av = new Avatar(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            User u = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
            u.setAvatar(av);
            userRepository.save(u);
            return new ResponseEntity("Avatar successfully updated", HttpStatus.OK);
        }
        return new ResponseEntity("Avatar not uploaded", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> removeAvatar(UserDetails userDetails) {
        User u = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userDetails.getUsername()));
        u.setAvatar(null);
        userRepository.save(u);
        return new ResponseEntity("Avatar successfully removed", HttpStatus.OK);
    }

    private boolean checkSize(MultipartFile file, long size){
        if(file.getSize() > size || file.isEmpty())
            return true;
        return false;
    }

    private BufferedImage fromMultipartFileToBufferedImage(MultipartFile file){
        BufferedImage bf = null;
        try{
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException e){
            return null;
        }
    }

    private boolean checkDimensions(int width, int height, MultipartFile file){
        BufferedImage bf = fromMultipartFileToBufferedImage(file);
        if(bf != null){
            if(bf.getWidth() > width || bf.getHeight() > height)
                return true;
        }
        return false;
    }

    private boolean checkExtensions(MultipartFile file, String[] extensions){
        String filename = file.getOriginalFilename(); // filename = pippo.jpeg.png
        String ext = null;
        try{
            ext = filename.substring(filename.lastIndexOf(".")+1);
            if(Arrays.stream(extensions).anyMatch(ext::equalsIgnoreCase))
                return false;
        } catch(NullPointerException e){
            return true;
        }
        return true;
    }

    protected Image geImageById(int imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
    }


}
