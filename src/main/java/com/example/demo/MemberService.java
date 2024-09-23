package com.example.demo;

import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public Member insertMember(@RequestBody Member member) {

        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public List<Member> SelectAllMembers(){
        List<Member> members = memberRepository.findAll();
        return members;
    }

    public Member SelectAllMemberByName(String name){
        Member member = memberRepository.findFirstByName(name);
        return member;
    }

    public Member SelectMemberById(Integer id){
        Member member = memberRepository.findById(id);
        return member;
    }
}
