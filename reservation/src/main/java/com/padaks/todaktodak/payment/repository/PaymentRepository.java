package com.padaks.todaktodak.payment.repository;

import com.padaks.todaktodak.payment.domain.Pay;
import com.padaks.todaktodak.payment.domain.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Pay, Long> {
    Pay findByImpUid(String impUid);
    Page<Pay> findAll(Pageable pageable);
    List<Pay> findByPaymentMethod(PaymentMethod paymentMethod);
}
