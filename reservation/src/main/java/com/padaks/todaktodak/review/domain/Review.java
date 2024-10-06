package com.padaks.todaktodak.review.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.review.dto.ReviewListResDto;
import com.padaks.todaktodak.review.dto.ReviewMyListResDto;
import com.padaks.todaktodak.review.dto.ReviewUpdateReqDto;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String memberEmail;
    private String name; // memberName

    private String doctorName;
//    @Column(nullable = false)
//    @Check(constraints = "value BETWEEN 1 AND 5")
    private int rating;

    @Column
    private String contents;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public ReviewListResDto listFromEntity() {
        return ReviewListResDto.builder()
                .id(this.id)
                .hospitalName(this.reservation.getHospital().getName())
                .doctorName(this.reservation.getDoctorName())
                .rating(this.rating)
                .contents(this.contents)
                .name(this.name)
                .createdAt(this.getCreatedAt())
                .build();
    }

    public ReviewMyListResDto myListFromEntity(){
        return ReviewMyListResDto.builder()
                .id(this.id)
                .hospitalName(this.reservation.getHospital().getName())
                .doctorName(this.reservation.getDoctorName())
                .name(this.name)
                .rating(this.rating)
                .contents(this.contents)
                .createdAt(this.getCreatedAt())
                .build();
    }

    public void updateReview(ReviewUpdateReqDto dto){
        this.rating = dto.getRating();
        this.contents = dto.getContents();
    }
}
