package com.web6.server.repository;

import com.web6.server.domain.MovieArticle;
import com.web6.server.dto.MovieArticleDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieArticleRepository extends JpaRepository<MovieArticle, Long> {
    boolean existsByMovieSeq(String movieSeq);
    MovieArticle findByMovieSeq(String movieSeq);
  
    boolean existsByid(Long id);
    // 댓글순 정렬
    @Query("SELECT new com.web6.server.dto.MovieArticleDTO(m.title, m.gradeCount) FROM MovieArticle m ORDER BY m.gradeCount DESC, m.title ASC")
    Page<MovieArticleDTO> findOrderByGradeCountDesc(Pageable pageable);
}