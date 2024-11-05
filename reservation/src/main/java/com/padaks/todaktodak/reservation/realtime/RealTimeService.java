package com.padaks.todaktodak.reservation.realtime;

import com.google.firebase.database.*;
import com.padaks.todaktodak.reservation.dto.RedisDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@DependsOn("firebaseConfig")
public class RealTimeService {

    private final FirebaseDatabase database= FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference= database.getReference("todakpadak");
    private final RedisTemplate<String, Object> redisTemplate;

    public RealTimeService(@Qualifier("1") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    //    test API 에 사용
//    public void updateWaitingLine(List<WaitingTurnDto> turnList){
//        for(WaitingTurnDto waitingTurnDto : turnList){
//            update(waitingTurnDto);
//        }
//    }

    //  데이터 중 특정 필드만 업데이트하는 메서드
    public void update(WaitingTurnDto waitingTurnDto, RedisDto redisDto) {
        System.out.println(waitingTurnDto.toString());
        DatabaseReference doctorRef = databaseReference
                .child(waitingTurnDto.getHospitalName())
                .child(waitingTurnDto.getDoctorId());

        String key = waitingTurnDto.getKey();

        Long myTurn = redisTemplate.opsForZSet().rank(key, redisDto);

        System.out.println("내 순서 " + myTurn);
        Map<String, Object> updates = new HashMap<>();
        updates.put("id", waitingTurnDto.getReservationId());
        updates.put("myTurn", myTurn);

        doctorRef.child(waitingTurnDto.getReservationId()).updateChildren(updates, (error, ref) -> {
            if (error != null) {
                System.out.println("Failed to update data: " + error.getMessage());
                // 충돌 처리: 다른 요청이 업데이트를 이미 한 경우 처리 로직
            } else {
                System.out.println("User data updated successfully");
                redisTemplate.opsForValue().increment(key); // Redis에 대기 순서 증가
            }
        });
    }

    //  실시간 DB에서 삭제하는 로직
    public void delete(String hospitalName, String doctorId, String id){
        DatabaseReference doctorRef = databaseReference.child(hospitalName).child(doctorId);

        // 삭제할 항목의 turn 값 찾기
        doctorRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long deletedTurn = dataSnapshot.child("turn").getValue(Long.class);  // 삭제할 항목의 turn 값

                    // 삭제 후 turn 값을 업데이트할 항목들 찾기
                    doctorRef.orderByChild("turn").startAt(deletedTurn + 1)
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        Long currentTurn = child.child("turn").getValue(Long.class);
                                        if (currentTurn != null) {
                                            // turn 값을 1씩 줄임
                                            child.getRef().child("turn").setValue(currentTurn - 1, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError error, DatabaseReference ref) {
                                                    if(error != null){
                                                        System.out.println("Failed update : " + error.getMessage());
                                                    }else{
                                                        System.out.println("turn update : " + ref.getKey());
                                                    }
                                                }
                                            });
                                        }else{
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