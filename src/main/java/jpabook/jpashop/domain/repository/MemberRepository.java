package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor//em생성하고 Autowired까지 원래 PersistenceContext 써야대는데 Spring에서 autowired써도 지원
public class MemberRepository {


    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }
    public Member fineOne(Long id){
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){

        return em.createQuery("select m from Member m", Member.class).getResultList();

    }
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name=:name", Member.class).setParameter("name",name).getResultList();
    }

}
