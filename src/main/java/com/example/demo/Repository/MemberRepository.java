package com.example.demo.Repository;

import com.example.demo.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findFirst2ByNameLikeOrderByIdDesc(String name);
    List<Member> findAll();
    Member findByName(String name);

    Member findFirstByName(String name);
    Member findById(Integer id);
}
