package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Category;
import it.cgmconsulting.myblog.entity.Content;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.exception.OwnerException;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.response.ContentResponse;
import it.cgmconsulting.myblog.payload.response.PostBoxResponse;
import it.cgmconsulting.myblog.payload.response.PostDetail;
import it.cgmconsulting.myblog.payload.response.PostHomePageResponse;
import it.cgmconsulting.myblog.repository.ContentRepository;
import it.cgmconsulting.myblog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${application.contentImage.path}")
    private String imagePath;

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final ContentRepository contentRepository;

    public ResponseEntity<?> addPost(UserDetails userDetails, byte categoryId) {
        User u = (User) userDetails;
        Category cat = categoryService.getCategoryByIdAndVisibleTrue(categoryId);
        Post p = new Post(u, cat);
        postRepository.save(p);
        return new ResponseEntity("New post created", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updatePostCategory(int postId, byte categoryId, UserDetails userDetails) {
        Post p = getPostById(postId);
        isOwner(userDetails, p.getAuthor());
        Category cat = categoryService.getCategoryByIdAndVisibleTrue(categoryId);
        p.setCategory(cat);
        return new ResponseEntity("Category updated", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> setPublicationDate(UserDetails userDetails, int postId, LocalDateTime publicationDate) {
        Post p = getPostById(postId);
        isOwner(userDetails, p.getAuthor());
        p.setPublicationDate(publicationDate);
        return new ResponseEntity("Publication date has been updated", HttpStatus.OK);
    }

    protected Post getPostById(int postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    protected void isOwner(UserDetails userDetails, User user) {
        if (!user.equals((User) userDetails)) 
            throw new OwnerException();
    }

    public ResponseEntity<?> getPostHomePageList(){
        List<PostHomePageResponse> list = postRepository.getPostHomePageList(LocalDateTime.now(), imagePath);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostBoxList(){
        List<PostBoxResponse> list = postRepository.getPostBoxList(LocalDateTime.now(), imagePath);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostDetail(int postId){
        PostDetail p = postRepository.getPostDetail(postId, LocalDateTime.now(), imagePath);
        List<Content> contents = contentRepository.getPostDetailContents(postId);
        List<ContentResponse> contentList = contents.stream().map( c -> {
            return new ContentResponse(c.getTitle(), c.getContent(), c.getImages());
        }).collect(Collectors.toList());
        Content content = contentRepository.findByTypeAndPostId('F', postId);
        ContentResponse contentf = new ContentResponse(content.getTitle(), content.getContent(), content.getImages());

        p.setContents(contentList);
        p.setContentF(contentf);
        return new ResponseEntity(p, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostsByCategory(String categoryName, int pageNumber, int pageSize, String sortBy, String direction){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostBoxResponse> result = postRepository.getPostsByCategory(pageable, categoryName, LocalDateTime.now(), imagePath);
        if(result.hasContent())
            return new ResponseEntity(result.getContent(), HttpStatus.OK);
        return new ResponseEntity("No posts found with category "+categoryName, HttpStatus.NOT_FOUND);
    }

    protected void isPostVisible(Post post) {
        if (LocalDateTime.now().isBefore(post.getPublicationDate()))
            throw new ResourceNotFoundException("Post", "id", post.getId());
    }
}
