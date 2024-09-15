package com.padaks.todaktodak.child.dto;

import com.padaks.todaktodak.child.domain.Child;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildResDto {
    private Long id;
    private String name;
    private String ssn;

    public ChildResDto fromEntity(Child child) {
        return ChildResDto.builder()
                .id(child.getId())
                .name(child.getName())
                .ssn(child.getSsn())
                .build();
    }
}
