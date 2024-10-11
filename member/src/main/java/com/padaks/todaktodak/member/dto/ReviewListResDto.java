package com.padaks.todaktodak.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewListResDto {
    private Long id;
    private String name;
    private String hospitalName;
    private String doctorName;
    private int rating;
    private String contents;
    private LocalDateTime createdAt;
    private boolean untact;
}
