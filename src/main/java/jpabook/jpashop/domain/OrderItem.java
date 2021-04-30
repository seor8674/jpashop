package jpabook.jpashop.domain;


import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    protected OrderItem(){

    }

    //생성 메서드
    public static OrderItem createOrderItem(Item item,int orderprice,int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderprice);
        orderItem.setCount(count);
        item.removeStock(count);
        return orderItem;
    }
    //비지니스로직
    public void cancel() {
        getItem().addStock(count);
    }

    //조회 로직
    public int getTotalprice() {
        return getOrderPrice()*getCount();
    }
}
