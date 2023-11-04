package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.payload.response.CommentResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public ResponseEntity<?> addComment(UserDetails userDetails, CommentRequest request) {
        Post p = postService.getPostById(request.getPostId());
        postService.isPostVisible(p);
        Comment c = new Comment((User) userDetails, p, request.getComment());
        p.addComment(c);
        return new ResponseEntity("New comment added to post", HttpStatus.CREATED);
    }

    public ResponseEntity<?> getComments(int postId) {
        Post p = postService.getPostById(postId);
        postService.isPostVisible(p);
        List<CommentResponse> list = commentRepository.getComments(postId);
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
