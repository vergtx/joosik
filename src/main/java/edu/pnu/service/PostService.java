package edu.pnu.service;

import java.util.List;

import edu.pnu.domain.Post;
import edu.pnu.dto.PostRequest;

public interface PostService {

	List<Post> getPostsByStockCode(String stockCode);

	Post createPost(PostRequest postRequest);

	Post updatePost(Long postId, PostRequest postRequest);

	void deletePost(Long postId);
	
	
	
	
}