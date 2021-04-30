package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);
        //when
        int ordercount=2;
        Long orderId = orderService.order(member.getId(), book.getId(), ordercount);


        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER,getOrder.getStatus());
        assertEquals(1,getOrder.getOrderItems().size());
        assertEquals(10000*ordercount,getOrder.getTotalPrice());
        assertEquals(8,book.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-124"));
        em.persist(member);
        return member;
    }

    @Test
    public void 주문취소() throws  Exception{
        //given
        Member member =createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int ordercount=2;
        Long orderId=orderService.order(member.getId(), book.getId(), ordercount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals(book.getStockQuantity(),10);
    }
    @Test
    public void 상품주문_재고초과() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);
        int ordercount=11;

        //when

        //then
        NotEnoughStockException ex=assertThrows(NotEnoughStockException.class,() ->{
            orderService.order(member.getId(),book.getId(),ordercount);
        });
        assertEquals(ex.getMessage(),"need more stock");





    }
}