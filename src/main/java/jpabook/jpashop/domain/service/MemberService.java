package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)//읽기전용
@RequiredArgsConstructor// final 이 붙은 애들만 자동으로 생성자 만들어줌 그럼 그 생성자는 자동으로 Autowired된다.
public class MemberService {


    private final MemberRepository memberRepository;


    //회원가입
    @Transactional//데이터의 변경이있어서 readOnly 아닐때
    public Long join(Member member){
        validateDuplicateMember(member);//중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원조회

    public List<Member> findmembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberid){
        return memberRepository.fineOne(memberid);
    }
}
