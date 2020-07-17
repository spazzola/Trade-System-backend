package com.tradesystem.invoice;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.orderdetails.OrderDetails;
import com.tradesystem.payment.Payment;
import com.tradesystem.supplier.Supplier;
import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
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
    private boolean toEqualizeNegativeInvoice;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "supplier_fk")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "buyer_fk")
    private Buyer buyer;

    @OneToMany(mappedBy = "buyerInvoice")
    private List<Payment> buyerPayments;

    @OneToMany(mappedBy = "supplierInvoice")
    private List<Payment> supplierPayments;


    public Invoice() {
    }

}
