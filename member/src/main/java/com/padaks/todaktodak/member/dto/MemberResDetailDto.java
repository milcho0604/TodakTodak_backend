//package com.padaks.todaktodak.member.dto;
//
//import com.padaks.todaktodak.member.domain.Member;
//import com.padaks.todaktodak.member.domain.Role;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class MemberResDetailDto {
//
//    private Long id;
//    private String memberEmail;
//    private String name;
//    private Role role;
//    private LocalDateTime deletedAt;
//    private boolean isVerified;
//
//
//    public static MemberResDetailDto fromEntity(Member member) {
//        return MemberResDetailDto.builder()
//                .id(member.getId())
//                .memberEmail(member.getMemberEmail())
//                .name(member.getName())
//                .role(member.getRole())
//                .deletedAt(member.getDeletedAt())
//                .isVerified(member.isVerified())
//                .build();
//    }
//}
