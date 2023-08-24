package edu.pnu.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.Post;
import edu.pnu.dto.PostRequest;
import edu.pnu.persistence.PostRepository;

@Service
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;

	@Autowired
	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public List<Post> getPostsByStockCode(String stockCode) {
		return postRepository.findByStockCode(stockCode);
	}

	@Override
	public Post createPost(PostRequest postRequest) {
		// 요청으로부터 Post 엔티티 생성
		Post post = new Post();
		post.setStockCode(postRequest.getStockCode());
		post.setTitle(postRequest.getTitle());
		post.setWriter(postRequest.getWriter());
		post.setContent(postRequest.getContent());
		post.setCreateDate(new Date());
		post.setCnt(0L); // 새로운 게시글이므로 초기값은 0

		postRepository.save(post);

		return post;
	}

	@Override
	public Post updatePost(Long postId, PostRequest postRequest) {
		Optional<Post> optionalPost = postRepository.findById(postId);
		if (optionalPost.isPresent()) {
			Post post = optionalPost.get();
			post.setTitle(postRequest.getTitle());
			post.setWriter(postRequest.getWriter());
			post.setContent(postRequest.getContent());
			return postRepository.save(post);
		} else {
			return null;
		}
	}

	@Override
	public void deletePost(Long postId) {
		postRepository.deleteById(postId);
	}



}