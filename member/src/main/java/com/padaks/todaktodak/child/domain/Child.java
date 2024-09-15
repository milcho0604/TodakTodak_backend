package com.padaks.todaktodak.child.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Child extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String ssn;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<ChildParentsRelationship> childParentsRelationshipList;

    public void updateName(String name) {
        this.name = name;
    }

    public void delete() {
        this.setDeletedTimeAt(LocalDateTime.now());
    }
}
