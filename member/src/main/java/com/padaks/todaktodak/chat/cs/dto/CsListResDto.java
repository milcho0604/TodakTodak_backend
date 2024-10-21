package com.padaks.todaktodak.chat.cs.dto;

import com.padaks.todaktodak.chat.cs.domain.Cs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CsListResDto {

    private Long id;

    private String csContents;

    private String csStatus;

    private Long chatRoomId;

    private String memberEmail;

    private String memberName;


}
