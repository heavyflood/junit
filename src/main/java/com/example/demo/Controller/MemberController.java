package com.example.demo.Controller;

import com.example.demo.Entity.Member;
import com.example.demo.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/member/insert")
    @Transactional
    public ResponseEntity<Member> insertMember(@RequestBody Member member) {

        return ResponseEntity.ok(memberService.insertMember(member));
    }

    @GetMapping("/member/list")
    public List<Member> SelectAllMembers(){
        List<Member> members = memberService.SelectAllMembers();
        return members;
    }

    @GetMapping("/member/{name}")
    public ResponseEntity<Member> SelectAllMemberByName(@PathVariable String name){
        Member member = memberService.SelectAllMemberByName(name);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/member/id/{id}")
    public ResponseEntity<Member> SelectMemberById(@PathVariable Integer id){
        Member member = memberService.SelectMemberById(id);
        return ResponseEntity.ok(member);
    }
}
