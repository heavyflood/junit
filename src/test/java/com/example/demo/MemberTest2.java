package com.example.demo;

import com.example.demo.Entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnabledIfSystemProperty(named = "env", matches = "QA")
//@EnabledOnOs(OS.LINUX)
public class MemberTest2 {

    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    TestRestTemplate restTemplate;

    private ObjectMapper mapper;
    private TransactionStatus status;

    @BeforeEach
    void init(){
        status = transactionManager.getTransaction(new DefaultTransactionAttribute());
    }

    @AfterEach
        void rollback(){
        transactionManager.rollback(status);
    }

    @Test
    @DisplayName("멤버 생성 테스트")
    @Transactional
    public void memberCreationTest() throws Exception{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Member member = Member.builder().name("phs").age(20).build();

        //when
        HttpEntity<Member> req = new HttpEntity<>(member, headers);
        ResponseEntity<Member> res = restTemplate.postForEntity("/member/insert", req, Member.class);
        System.out.println(res);

        //then
        System.out.println("Result:::" + res.getStatusCode().toString() + ">>> "+ res.getBody().getId().toString());
        Assertions.assertEquals(200, res.getStatusCode().value());
        Assertions.assertEquals(res.getBody().getName(), member.getName());
        Assertions.assertEquals(res.getBody().getAge(), member.getAge());
    }

    @Test
    @DisplayName("멤버 목록")
    public void memberList() throws Exception{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        //when
        ResponseEntity<List<Member>> res = restTemplate.exchange("/member/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Member>>(){});
        System.out.println(res);

        //then
        System.out.println("Result:::" + res.getStatusCode().toString() + ">>> "+ res.getBody().size());

        Assertions.assertEquals(200, res.getStatusCode().value());
        Assertions.assertTrue(res.getBody().size() > 0);
    }

    @Test
    @DisplayName("멤버 조회")
    public void getMember() throws Exception{

        //given
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        //when
        ResponseEntity<Member> res = restTemplate.getForEntity("/member/박홍수", Member.class);
        System.out.println(res);

        //then
        System.out.println("Result:::" + res.getStatusCode().toString() + ">>> "+ res.getBody().getId());
        Assertions.assertEquals(200, res.getStatusCode().value());
    }

}
