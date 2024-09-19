package com.padaks.todaktodak.child.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildShareReqDto {
    private Long childId;
    private Long sharedId;
}
