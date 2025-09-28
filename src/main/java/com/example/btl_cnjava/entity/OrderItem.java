package com.example.btl_cnjava.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "snack_id")
    private Snack snack;


    private int quantity;

    private BigDecimal price; // Giá tại thời điểm đặt

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Snack getSnack() { return snack; }
    public void setSnack(Snack snack) { this.snack = snack; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    // ✅ Thêm phương thức này để tính thành tiền của mỗi OrderItem
    public BigDecimal getTotalPrice() {
        if (price == null || quantity <= 0) return BigDecimal.ZERO;
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
