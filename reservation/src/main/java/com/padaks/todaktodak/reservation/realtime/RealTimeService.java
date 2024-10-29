package com.padaks.todaktodak.reservation.realtime;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Lazy
public class RealTimeService {

    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;

    public RealTimeService() {
        // Firebase 초기화
        initializeFirebase();

        // FirebaseDatabase 인스턴스 가져오기
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference("todakpadak");
    }

    private void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("todak-1f8d0-firebase-adminsdk-tbqa8-b7c41789c9.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized successfully");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    // test API 에 사용
    public void updateWaitingLine(List<WaitingTurnDto> turnList){
        for(WaitingTurnDto waitingTurnDto : turnList){
            update(waitingTurnDto);
        }
    }

    // 데이터 중 특정 필드만 업데이트하는 메서드
    public void update(WaitingTurnDto waitingTurnDto) {
        System.out.println(waitingTurnDto.toString());
        DatabaseReference doctorRef = databaseReference
                .child(waitingTurnDto.getHospitalName())
                .child(waitingTurnDto.getDoctorId());

        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long queueSize = dataSnapshot.getChildrenCount();
                long myTurn = queueSize + 1;

                Map<String, Object> updates = new HashMap<>();
                updates.put("id", waitingTurnDto.getReservationId());
                updates.put("turn", myTurn);

                // Firebase에서 데이터 업데이트
                doctorRef.child(waitingTurnDto.getReservationId()).updateChildren(updates, (error, ref) -> {
                    if (error != null) {
                        System.out.println("Failed to update data: " + error.getMessage());
                    } else {
                        System.out.println("User data updated successfully");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read queue size: " + databaseError.getMessage());
            }
        });
    }

    // 실시간 DB에서 삭제하는 로직
    public void delete(String hospitalName, String doctorId, String id){
        DatabaseReference doctorRef = databaseReference.child(hospitalName).child(doctorId);

        // 삭제할 항목의 turn 값 찾기
        doctorRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long deletedTurn = dataSnapshot.child("turn").getValue(Long.class);

                    // 삭제 후 turn 값을 업데이트할 항목들 찾기
                    doctorRef.orderByChild("turn").startAt(deletedTurn + 1)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        Long currentTurn = child.child("turn").getValue(Long.class);
                                        if (currentTurn != null) {
                                            child.getRef().child("turn").setValue(currentTurn - 1, (error, ref) -> {
                                                if(error != null){
                                                    System.out.println("Failed update : " + error.getMessage());
                                                }else{
                                                    System.out.println("turn update : " + ref.getKey());
                                                }
                                            });
                                        } else {
                                            System.out.println("Turn is null for child : " + child.getKey());
                                        }
                                    }

                                    // 항목 삭제
                                    doctorRef.child(id).removeValue((error, ref) -> {
                                        if (error != null) {
                                            System.out.println("Failed to delete data: " + error.getMessage());
                                        } else {
                                            System.out.println("User data deleted successfully");
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    System.out.println("Failed to adjust turn: " + error.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to get deleted item turn: " + databaseError.getMessage());
            }
        });
    }

    // 데이터를 한 번 읽어오는 메서드
    public void readDataOnce(String id) {
        databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Message message = snapshot.getValue(Message.class);
                        if (message != null) {
                            System.out.println("Data found: " + message);
                        }
                    }
                } else {
                    System.out.println("No data found with id: " + id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}
