package com.padaks.todaktodak.relationship.domain;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChildParentsRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long id;

//    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
//    위 명령어는 외래키를 테이블 생성 한 후에 주입을 하겠다.
    @ManyToOne
    @JoinColumn(name = "child_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Child child;

    @ManyToOne
    @JoinColumn(name = "parents_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

}
