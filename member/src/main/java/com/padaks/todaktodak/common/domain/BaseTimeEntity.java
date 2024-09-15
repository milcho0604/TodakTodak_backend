package com.padaks.todaktodak.common.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

    @CreationTimestamp
    private LocalDateTime createdTimeAt;

    @CreationTimestamp
    private LocalDateTime updatedTimeAt;

    private LocalDateTime deletedTimeAt;

    // deletedTimeAt을 설정하는 메서드를 추가(기존 코드에는 deletedTImeAt이 생성시에 들어감)
    public void setDeletedTimeAt(LocalDateTime deletedTimeAt) {
        this.deletedTimeAt = deletedTimeAt;
    }
}
