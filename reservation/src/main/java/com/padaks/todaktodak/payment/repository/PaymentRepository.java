package com.padaks.todaktodak.payment.repository;

import com.padaks.todaktodak.payment.domain.Pay;
import com.padaks.todaktodak.payment.domain.PaymentMethod;
import com.padaks.todaktodak.payment.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Pay, Long> {
    Pay findByImpUid(String impUid);
    Page<Pay> findAll(Pageable pageable);
    List<Pay> findByPaymentMethodAndPaymentStatus(PaymentMethod paymentMethod, PaymentStatus paymentStatus);
    // impUid와 memberEmail로 검색하고, PaymentMethod로 필터링하는 쿼리 메서드
    // impUid 또는 memberEmail로 검색
    Page<Pay> findByImpUidContainingOrMemberEmailContaining(String impUid, String memberEmail, Pageable pageable);

    // impUid 또는 memberEmail과 PaymentMethod로 검색
    Page<Pay> findByImpUidContainingOrMemberEmailContainingAndPaymentMethod(String impUid, String memberEmail, PaymentMethod paymentMethod, Pageable pageable);
    Page<Pay> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
}
