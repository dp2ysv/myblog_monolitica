package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.payload.request.CommentRequest;
import it.cgmconsulting.myblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @CacheEvict(value = "tuttiICommenti", allEntries = true)
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentRequest request){
        return commentService.addComment(userDetails, request);
    }

    // lista commenti di un post
    @Cacheable("tuttiICommenti")
    @GetMapping("/public/{postId}")
    public ResponseEntity<?> getComments(@PathVariable int postId){
        log.info("Questa riga di log apparirà solo alla prima request poi non più finchè non viene aggiunto un nuovo commento\n in quanto il risultato della chiamata è stato cacheato");
        return commentService.getComments(postId);
    }

}
