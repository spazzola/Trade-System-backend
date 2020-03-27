package com.tradesystem.invoice;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.supplier.Supplier;
import lombok.Data;
import org.hibernate.annotations.Proxy;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    private String invoiceNumber;
    private LocalDate date;
    private BigDecimal value;
    private BigDecimal amountToUse;
    private boolean isUsed;
    private boolean isPaid;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    public Invoice() {
    }
}
