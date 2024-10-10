package com.padaks.todaktodak.post.repository;

import com.padaks.todaktodak.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByDeletedTimeAtIsNull(Pageable pageable);

    // 사용자의 이메일을 기준으로 삭제되지 않은 게시글 목록 조회
    Page<Post> findByMemberEmailAndDeletedTimeAtIsNull(String memberEmail, Pageable pageable);
}
