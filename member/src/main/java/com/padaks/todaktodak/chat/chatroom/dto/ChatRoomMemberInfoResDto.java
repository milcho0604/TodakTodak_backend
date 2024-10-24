package com.padaks.todaktodak.chat.chatroom.dto;

import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomMemberInfoResDto {
    // 채팅방에 참여하고 있는 회원의 정보 (채팅방 id로 참여자 정보 조회)
    private Long memberId; // 회원 id

    private String name; // 회원 이름

    private String profileImgUrl; // 회원 프로필 이미지

    public static ChatRoomMemberInfoResDto fromEntity(Member member){
        return builder()
                .memberId(member.getId())
                .name(member.getName())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }
}
