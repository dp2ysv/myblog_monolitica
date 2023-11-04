package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class PostDetail {

    private int postId;
    private String categoryName;
    private String username; // author

    // content di tipo H
    private String title;
    private String image;

    // contents di tipo C ordinati per progressivo ASC
    List<ContentResponse> contents;

    // content di tipo F
    private ContentResponse contentF;

    private double average;
    private long comments;

    public PostDetail(int postId, String categoryName, String username, String title, String image, double average, long comments) {
        this.postId = postId;
        this.categoryName = categoryName;
        this.username = username;
        this.title = title;
        this.image = image;
        this.average = average;
        this.comments = comments;
    }
}
