package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 선언한 클래스의 빌더 패턴을 가지는 클래스를 생성합니다.
@Entity // User라는 객체와 DB 테이블을 매핑합니다. JPA가 관리합니다.
public class Member {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 30)
        private String name;

        @Column(nullable = false)
        private int age;
}
