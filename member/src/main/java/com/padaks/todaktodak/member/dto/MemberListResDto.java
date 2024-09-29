package com.padaks.todaktodak.member.dto;


import com.padaks.todaktodak.member.domain.Address;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberListResDto {
    private Long id;
    private String name;
    private String phone;
    private Address address;
    private String memberEmail;
    private Role role;
    private boolean verified;

}
