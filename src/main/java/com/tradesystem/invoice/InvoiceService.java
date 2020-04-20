package com.tradesystem.invoice;

import com.tradesystem.buyer.Buyer;
import com.tradesystem.buyer.BuyerDao;
import com.tradesystem.buyer.BuyerDto;
import com.tradesystem.supplier.Supplier;
import com.tradesystem.supplier.SupplierDao;
import com.tradesystem.supplier.SupplierDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;


@Service
public class InvoiceService {

    private InvoiceDao invoiceDao;
    private BuyerDao buyerDao;
    private SupplierDao supplierDao;


    public InvoiceService(InvoiceDao invoiceDao, BuyerDao buyerDao, SupplierDao supplierDao) {
        this.invoiceDao = invoiceDao;
        this.buyerDao = buyerDao;
        this.supplierDao = supplierDao;
    }


    @Transactional
    public List<Invoice> getAll() {
        return invoiceDao.findAll();
    }

    @Transactional
    public Invoice createInvoice(InvoiceDto invoiceDto) {
        Buyer buyer = null;
        final BuyerDto buyerDto = invoiceDto.getBuyer();
        if (buyerDto != null) {
            buyer = buyerDao.findById(buyerDto.getId())
                    .orElseThrow(RuntimeException::new);
        }

        Supplier supplier = null;
        final SupplierDto supplierDto = invoiceDto.getSupplier();
        if (supplierDto != null) {
            supplier = supplierDao.findById(supplierDto.getId())
                    .orElseThrow(RuntimeException::new);
        }

        if (validateInvoice(invoiceDto, buyer, supplier)) {
            final Invoice invoice = Invoice.builder()
                    .id(null)
                    .invoiceNumber(invoiceDto.getInvoiceNumber())
                    .date(invoiceDto.getDate())
                    .value(invoiceDto.getValue())
                    .amountToUse(invoiceDto.getAmountToUse())
                    .isUsed(invoiceDto.isUsed())
                    .isPaid(invoiceDto.isPaid())
                    .comment(invoiceDto.getComment())
                    .buyer(buyer)
                    .supplier(supplier)
                    .build();

            if (invoice.getBuyer() != null) {
                processNegativeInvoicesForBuyer(invoice);
            } else {
                processNegativeInvoicesForSupplier(invoice);
            }

            return invoiceDao.save(invoice);
        } else {
            throw new RuntimeException("Can't create invoice");
        }
    }

    @Transactional
    public Invoice getInvoice(Long id) {
        Optional<Invoice> invoice = invoiceDao.findById(id);

        return invoice
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public List<Invoice> getInvoicesByMonth(int month, int year) {
        return invoiceDao.getMonthInvoices(month, year);
    }

    @Transactional
    public void payForInvoice(Long id) {
        Optional<Invoice> optionalInvoice = invoiceDao.findById(id);
        Invoice invoice = optionalInvoice
                .orElseThrow(NoSuchElementException::new);

        if (invoice.getAmountToUse().doubleValue() <= 0) {
            throw new RuntimeException("Nie mozna zaplacic negatywnej faktury");
        }
        boolean isBuyerOwner = checkInvoiceOwner(invoice);

        if (isBuyerOwner) {
            invoice.setPaid(true);
            processNegativeInvoicesForBuyer(invoice);
        } else {
            invoice.setPaid(true);
            processNegativeInvoicesForSupplier(invoice);
        }
    }

    private boolean checkInvoiceOwner(Invoice invoice) {
        return invoice.getBuyer() != null;
    }

    private void processNegativeInvoicesForBuyer(Invoice invoice) {

        if (invoice.isPaid()) {
            Long buyerId = invoice.getBuyer().getId();
            Optional<Invoice> negativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyerId);

            if (negativeInvoice.isPresent()) {
                negativeInvoice.get().setUsed(true);
                invoiceDao.save(negativeInvoice.get());

                BigDecimal negativeAmount = negativeInvoice.get().getAmountToUse();
                BigDecimal newAmount = invoice.getAmountToUse().add(negativeAmount);
                BigDecimal different = BigDecimal.valueOf(0.0);

                if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                    different = negativeAmount;
                }
                if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                    different = negativeAmount.subtract(newAmount);
                }

                if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                    invoice.setComment("Pomniejszono o " + different + " z faktury o id " + negativeInvoice.get().getId());
                }
                if (newAmount.compareTo(BigDecimal.ZERO) >= 0) {
                    invoice.setComment("Pomniejszono o " + negativeAmount + " z faktury o id " + negativeInvoice.get().getId());
                }

                saveInvoice(invoice, newAmount, invoice.getValue());
            }
        } else {
            saveInvoice(invoice, invoice.getValue(), invoice.getValue());
        }
    }

    private void processNegativeInvoicesForSupplier(Invoice invoice) {

        if (invoice.isPaid()) {
            Long supplierId = invoice.getSupplier().getId();
            Optional<Invoice> negativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplierId);

            if (negativeInvoice.isPresent()) {
                negativeInvoice.get().setUsed(true);
                invoiceDao.save(negativeInvoice.get());

                BigDecimal negativeAmount = negativeInvoice.get().getAmountToUse();
                BigDecimal newAmount = invoice.getAmountToUse().add(negativeAmount);
                BigDecimal different = BigDecimal.valueOf(0.0);

                if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                    different = negativeAmount;
                }
                if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                    different = negativeAmount.subtract(newAmount);

                }

                if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                    invoice.setComment("Pomniejszono o " + different + " z faktury o id " + negativeInvoice.get().getId());
                }
                if (newAmount.compareTo(BigDecimal.ZERO) >= 0) {
                    invoice.setComment("Pomniejszono o " + negativeAmount + " z faktury o id " + negativeInvoice.get().getId());
                }

                saveInvoice(invoice, newAmount, invoice.getValue());
            }
        } else {
            saveInvoice(invoice, invoice.getValue(), invoice.getValue());
        }
    }

    private void saveInvoice(Invoice invoice, BigDecimal newAmount, BigDecimal value) {
        invoice.setAmountToUse(newAmount);
        invoice.setValue(invoice.getValue());
        invoiceDao.save(invoice);
    }

    @Transactional
    public void trasnferInvoicesToNextMonth(LocalDate localDate) {
        transferBuyerInvoices(localDate);
        transferSupplierInvoices(localDate);
    }

    private void transferSupplierInvoices(LocalDate localDate) {
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        Optional<List<Invoice>> optionalInvoices = invoiceDao.getSuppliersMonthNotUsedInvoices(month - 1, year);
        if (optionalInvoices.isPresent()) {
            for (Invoice oldInvoice : optionalInvoices.get()) {
                BigDecimal amountToUse = oldInvoice.getAmountToUse();
                String invoiceNumber = oldInvoice.getInvoiceNumber();
                Supplier supplier = oldInvoice.getSupplier();
                LocalDate date = oldInvoice.getDate();
                oldInvoice.setUsed(true);
                invoiceDao.save(oldInvoice);

                Invoice newInvoice = createInvoice(amountToUse, invoiceNumber, date);
                newInvoice.setSupplier(supplier);

                invoiceDao.save(newInvoice);
            }
        }
    }

    private void transferBuyerInvoices(LocalDate localDate) {
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        Optional<List<Invoice>> optionalInvoices = invoiceDao.getBuyersMonthNotUsedPositivesInvoices(month - 1, year);

        if (optionalInvoices.isPresent()) {
            for (Invoice oldInvoice : optionalInvoices.get()) {
                BigDecimal amountToUse = oldInvoice.getAmountToUse();
                String invoiceNumber = oldInvoice.getInvoiceNumber();
                Buyer buyer = oldInvoice.getBuyer();
                LocalDate date = oldInvoice.getDate();
                oldInvoice.setUsed(true);
                invoiceDao.save(oldInvoice);

                Invoice newInvoice = createInvoice(amountToUse, invoiceNumber, date);
                newInvoice.setValue(amountToUse);
                newInvoice.setBuyer(buyer);

                invoiceDao.save(newInvoice);
            }
        }
    }

    private Invoice createInvoice(BigDecimal amountToUse, String invoiceNumber, LocalDate date) {
        Invoice invoice = new Invoice();
        date = date.with(TemporalAdjusters.firstDayOfMonth());
        invoice.setAmountToUse(amountToUse);
        invoice.setInvoiceNumber(invoiceNumber + "-przeniesiona");
        invoice.setValue(amountToUse);
        invoice.setPaid(true);
        invoice.setUsed(false);
        invoice.setComment("Przeniesiono z poprzedniego miesiąca,  FV o nr: " + invoiceNumber);
        invoice.setDate(date.plusMonths(1));

        return invoice;
    }

    private boolean validateInvoice(InvoiceDto invoiceDto, Buyer buyer, Supplier supplier) {
        if (invoiceDto.getInvoiceNumber() == null || invoiceDto.getInvoiceNumber().equals("")) {
            return false;
        }
        if (invoiceDto.getDate() == null) {
            return false;
        }
        if (invoiceDto.getValue().doubleValue() <= 0) {
            return false;
        }
        if (invoiceDto.getAmountToUse().doubleValue() <= 0) {
            return false;
        }
        if (buyer != null && supplier != null) {
            throw new RuntimeException("Nie mozna stworzyc faktury dla buyera i suppliera naraz");
        }
        if (buyer == null && supplier == null) {
            throw new RuntimeException("Nie mozna stworzyc faktury bez buyera lub suppliera");
        }
        return true;
    }

}
