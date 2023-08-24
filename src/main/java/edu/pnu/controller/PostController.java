package edu.pnu.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.Post;
import edu.pnu.dto.ApiResponse;
import edu.pnu.dto.PostRequest;
import edu.pnu.persistence.PostRepository;
import edu.pnu.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {
	
	private final PostService postService;
	private final PostRepository postRepository;  // Add this line
	

	@Autowired
	public PostController(PostService postService, PostRepository postRepository) {
		this.postService = postService;
		this.postRepository = postRepository;
	}

	@GetMapping
	public List<Post> getPostsByStockCode(@RequestParam String stockCode) {
		return postService.getPostsByStockCode(stockCode);
	}

	@PostMapping
	public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
		String currentUsername = authentication.getName(); // username 누구의 이름인지 가져 오는것
	    postRequest.setWriter(currentUsername); // 현재 로그인한 사용자의 이름으로 작성자 설정
		
		log.debug("Received request to create post: {}", postRequest);
		Post createdPost = postService.createPost(postRequest);
		if (createdPost != null) {
			return ResponseEntity.ok(new ApiResponse(true, "Post created successfully"));
		} else {
			return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to create post"));
		}
	}
	//@PathVariable경로 변수인 postId를 메서드 매개변수로 받음 
	//@Valid 애노테이션은 입력값의 유효성 검사를 요청 여기서는 PostRequest매개변수로 전달되는 데이터에 대해 유효성 검사가 수행
	//@RequestBody는 HTTP 요청의 본문에 담긴 데이터를 메서드 매개변수로 받음 클라이언트가 POST 또는 PUT과 같은 HTTP 메서드를 사용하여 데이터를 요청 본문에 담아 서버로 보낼 때 사용
	
	//Authentication authentication 추가
	@PutMapping("/update/{postId}") 
	public ResponseEntity<?> updatePost(@PathVariable Long postId, @Valid @RequestBody PostRequest postRequest, Authentication authentication) {
	    Optional<Post> optionalPost = postRepository.findById(postId);
	    
	    if (optionalPost.isPresent()) {
	        Post post = optionalPost.get();
	        
	        // 현재 로그인한 사용자의 이름
	        String currentUsername = authentication.getName();
	        
	        // 작성자의 이름과 현재 로그인한 사용자의 이름 비교
	        if (post.getWriter().equals(currentUsername)) {
	            post.setTitle(postRequest.getTitle());
	            post.setWriter(postRequest.getWriter());
	            post.setContent(postRequest.getContent());
	            
	            postRepository.save(post);
	            return ResponseEntity.ok(new ApiResponse(true, "Post updated successfully"));
	        } else {
	            return ResponseEntity.badRequest().body(new ApiResponse(false, "You are not authorized to update this post"));
	        }
	    } else {
	        return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to update post"));
	    }
	}
	
	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId, Authentication authentication) {
		String currentUsername = authentication.getName();
		if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
			postService.deletePost(postId);
			return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully"));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Permission denied"));
		}
	}
}