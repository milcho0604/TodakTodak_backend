package com.padaks.todaktodak.payment.repository;

import com.padaks.todaktodak.payment.domain.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Pay, Long> {
}
