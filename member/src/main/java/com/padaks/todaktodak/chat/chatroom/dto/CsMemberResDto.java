package com.padaks.todaktodak.chat.chatroom.dto;

import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsMemberResDto {
    private Long id;
    private String name;
    private Role role;
    private String profileImgUrl;
}
