package com.padaks.todaktodak.member.domain;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column
    private Long familyId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String profileImgUrl;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String ssn;
    @Column(nullable = false)
    private String address;
    @ColumnDefault("0")
    private int noShowCount;
    @ColumnDefault("0")
    private int reportCount;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "Mid", cascade = CascadeType.ALL)
    private List<Child> childList;
}
