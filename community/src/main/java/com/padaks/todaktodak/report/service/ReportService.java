package com.padaks.todaktodak.report.service;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.comment.repository.CommentRepository;
import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.post.repository.PostRepository;
import com.padaks.todaktodak.report.domain.Report;
import com.padaks.todaktodak.report.dto.ReportSaveReqDto;
import com.padaks.todaktodak.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;


    public Report create(ReportSaveReqDto dto){
        Post post = null;
        Comment comment = null;
        if (dto.getPostId() != null){
            post = postRepository.findById(dto.getPostId()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 post입니다."));
            String postMemberEmail = post.getMemberEmail();
        } else if (dto.getCommentId() != null) {
            comment = commentRepository.findById(dto.getCommentId()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 comment입니다."));
            String commentDoctorEmail = comment.getDoctorEmail();
        }

        //member의 report count 어떻게 +1..???????

        Report report = dto.toEntity(post, comment);
        report = reportRepository.save(report);

        return  report;
    }
}
