package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveReqDto {
    private String name;
    private String email;
    private String password;
    @Builder.Default
    private Role role = Role.Member;
//        private DelYN delYN = DelYN.N;

    public Member toEntity(String password) {
        return Member.builder()
                .password(password)
                .name(this.name)
                .email(this.email)
                .role(this.role)
//                    .delYN(this.delYN)
                .build();

    }
}
