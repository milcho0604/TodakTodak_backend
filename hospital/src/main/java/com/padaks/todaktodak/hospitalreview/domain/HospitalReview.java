package com.padaks.todaktodak.hospitalreview.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class HospitalReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private String email;

//    친절
    @ColumnDefault("0")
    private int kindnessRating;
//    전문성
    @ColumnDefault("0")
    private int professionalTreatmentRating;
//    청결성
    @ColumnDefault("0")
    private int cleanlinessRating;
//    주차편의
    @ColumnDefault("0")
    private int parkingConvenienceRating;
//    접근성
    @ColumnDefault("0")
    private int accessibilityRating;

    @OneToOne
    @JoinColumn(name = "hospital_id")
    private Hospital Hid;
}
