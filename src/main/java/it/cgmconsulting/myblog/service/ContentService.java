package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Content;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ContentRequest;
import it.cgmconsulting.myblog.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ContentService {

    @Value("${application.contentTypes}")
    private char[] contetTypes;

    private final ContentRepository contentRepository;
    private final PostService postService;

    public ResponseEntity<?> createContent(ContentRequest request, UserDetails userDetails){
        for(char c : contetTypes) {
            if (request.getType() == c) {
                Post p = postService.getPostById(request.getPostId());
                postService.isOwner(userDetails, p.getAuthor());
                Content co = fromRequestToEntity(request, p);
                byte prg = 0;
                if (request.getType() == 'H' && contentRepository.existsByTypeAndPost('H', p))
                    return new ResponseEntity("Header for post " + p.getId() + " already created", HttpStatus.BAD_REQUEST);
                if (request.getType() == 'F' && contentRepository.existsByTypeAndPost('F', p))
                    return new ResponseEntity("Footer for post " + p.getId() + " already created", HttpStatus.BAD_REQUEST);

                prg = (byte) (contentRepository.getMaxPrgByType(p, request.getType()) + 3);
                co.setPrg(prg);
                contentRepository.save(co);
                return new ResponseEntity("Content added to post", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity("Content type not allowed", HttpStatus.BAD_REQUEST);
    }


    protected Content fromRequestToEntity(ContentRequest request, Post p){
        return new Content(
                request.getTitle(),
                request.getContent(),
                request.getType(),
                p);
    }

    protected Content getContentById(int contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", contentId));
    }
}
