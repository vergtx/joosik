package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import edu.pnu.domain.Post;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStockCode(String stockCode);
}