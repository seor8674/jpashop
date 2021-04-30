package jpabook.jpashop.domain.item;


import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Item {

    @Id
    @GeneratedValue
    @Column(name ="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;


    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //비지니스 로직

    //stock 증가
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }
    //stock 감소
    public void removeStock(int quantity){
        int i = this.stockQuantity - quantity;
        if(i<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=i;

    }
}
