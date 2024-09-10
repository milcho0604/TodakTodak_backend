package com.padaks.todaktodak.common.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

    @CreationTimestamp
    private LocalDateTime createdTimeAt;
    @UpdateTimestamp
    private LocalDateTime updatedTimeAt;
    @UpdateTimestamp
    private LocalDateTime deletedTimeAt;

}
