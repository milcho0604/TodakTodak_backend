package com.padaks.todaktodak.chatroom.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
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
public class UntactChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "untact_chat_room_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "medical_chart_id")
    private MedicalChart medicalChart;
}
