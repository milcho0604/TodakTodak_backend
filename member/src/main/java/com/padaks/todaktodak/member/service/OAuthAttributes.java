package com.padaks.todaktodak.member.service;


import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String memberEmail;
    private String pictureURL;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name, String memberEmail, String pictureURL) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.memberEmail = memberEmail;
        this.pictureURL = pictureURL;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofKakao(userNameAttributeName, attributes);
    }



    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        System.out.println("집중!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(attributes);
        System.out.println(attributes.toString());

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//        System.out.println("여기서 부터닦ㅆ%!!!!!!!!!!!!!!!!!!!");
        kakaoAccount.get(profile.get("nickname"));

        return OAuthAttributes.builder()
                .memberEmail((String) kakaoAccount.get("email"))
                .pictureURL((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .memberEmail(memberEmail)
                .role(Role.Member)
                .build();
    }
}
