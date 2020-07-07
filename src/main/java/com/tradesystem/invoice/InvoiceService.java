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
    public Invoice getInvoiceByInvoiceNumber(String invoiceNumber) {
        return invoiceDao.getByInvoiceNumber(invoiceNumber);
    }

    @Transactional
    public void payForInvoice(Long id) {
        Optional<Invoice> optionalInvoice = invoiceDao.findById(id);
        Invoice invoice = optionalInvoice
                .orElseThrow(NoSuchElementException::new);

        if (invoice.getAmountToUse().doubleValue() < 0) {
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

    @Transactional
    public void transferInvoicesToNextMonth(LocalDate localDate) {
        transferBuyerInvoices(localDate);
        transferSupplierInvoices(localDate);
    }

    @Transactional
    public BigDecimal getBuyersPositiveBalance() {
        Optional<List <Invoice>> optionalInvoices = invoiceDao.getBuyersPaidNotUsedPositiveInvoices();
        BigDecimal result = BigDecimal.valueOf(0);

        if (optionalInvoices.isPresent()) {
            for (Invoice invoice : optionalInvoices.get()) {
                result = result.add(invoice.getAmountToUse());
            }
        }
        return result;
    }

    @Transactional
    public BigDecimal getBuyersNegativeBalance() {
        Optional<List <Invoice>> optionalInvoices = invoiceDao.getBuyersPaidNotUsedNegativeInvoices();
        BigDecimal result = BigDecimal.valueOf(0);

        if (optionalInvoices.isPresent()) {
            for (Invoice invoice : optionalInvoices.get()) {
                result = result.add(invoice.getAmountToUse());
            }
        }

        Optional<List <Invoice>> notPaidInvoices = invoiceDao.getBuyersNotPaidInvoices();
        if (notPaidInvoices.isPresent()) {
            for (Invoice invoice : notPaidInvoices.get()) {
                result = result.subtract(invoice.getAmountToUse());
            }
        }
        return result;
    }

    @Transactional
    public BigDecimal getSuppliersPositiveBalance() {
        Optional<List <Invoice>> optionalInvoices = invoiceDao.getSuppliersPaidNotUsedPositiveInvoices();
        BigDecimal result = BigDecimal.valueOf(0);

        if (optionalInvoices.isPresent()) {
            for (Invoice invoice : optionalInvoices.get()) {
                result = result.add(invoice.getAmountToUse());
            }
        }
        return result;
    }

    @Transactional
    public BigDecimal getSuppliersNegativeBalance() {
        Optional<List <Invoice>> optionalInvoices = invoiceDao.getSuppliersPaidNotUsedNegativeInvoices();
        BigDecimal result = BigDecimal.valueOf(0);

        if (optionalInvoices.isPresent()) {
            for (Invoice invoice : optionalInvoices.get()) {
                result = result.add(invoice.getAmountToUse());
            }
        }
        return result;
    }

    @Transactional
    public List<Invoice> getBuyerMonthInvoices(Long buyerId, int month, int year) {
        return invoiceDao.getBuyerMonthInvoices(buyerId, month, year);
    }

    @Transactional
    public List<Invoice> getBuyersMonthInvoices(int month, int year) {
        return invoiceDao.getBuyersMonthInvoices(month, year);
    }

    @Transactional
    public List<Invoice> getSupplierMonthInvoices(Long supplierId, int month, int year) {
        return invoiceDao.getSupplierMonthInvoices(supplierId, month, year);
    }

    @Transactional
    public List<Invoice> getSuppliersMonthInvoices(int month, int year) {
        return invoiceDao.getSuppliersMonthInvoices(month, year);
    }

    private boolean checkInvoiceOwner(Invoice invoice) {
        return invoice.getBuyer() != null;
    }

    private void processNegativeInvoicesForBuyer(Invoice invoice) {

        if (invoice.isPaid()) {
            Long buyerId = invoice.getBuyer().getId();
            Optional<Invoice> optionalNegativeInvoice = invoiceDao.getBuyerNegativeInvoice(buyerId);

            optionalNegativeInvoice.ifPresent(negativeInvoice -> processNegativeInvoice(invoice, negativeInvoice));
        } else {
            saveInvoice(invoice, invoice.getValue(), false);
        }
    }

    private void processNegativeInvoicesForSupplier(Invoice invoice) {

        if (invoice.isPaid()) {
            Long supplierId = invoice.getSupplier().getId();
            Optional<Invoice> optionalNegativeInvoice = invoiceDao.getSupplierNegativeInvoice(supplierId);

            optionalNegativeInvoice.ifPresent(negativeInvoice -> processNegativeInvoice(invoice, negativeInvoice));
        } else {
            saveInvoice(invoice, invoice.getValue(), false);
        }
    }

    private void processNegativeInvoice(Invoice invoice, Invoice negativeInvoice) {
        BigDecimal converter = BigDecimal.valueOf(-1);
        BigDecimal zero = BigDecimal.ZERO;

        BigDecimal negativeAmount = negativeInvoice.getAmountToUse();
        BigDecimal convertedNegativeAmount = negativeAmount.multiply(converter);
        BigDecimal invoiceAmount = invoice.getAmountToUse();
        BigDecimal newAmount;

        if (invoiceAmount.compareTo(convertedNegativeAmount) == 0) {
            saveInvoice(negativeInvoice, zero, true);

            invoice.setComment("Pomniejszono o " + negativeAmount + " z faktury o id " + negativeInvoice.getId());
            saveInvoice(invoice, zero, true);
        }
        if (invoiceAmount.compareTo(convertedNegativeAmount) == 1) {
            saveInvoice(negativeInvoice, zero, true);

            newAmount = invoiceAmount.subtract(convertedNegativeAmount);
            invoice.setComment("Pomniejszono o " + negativeAmount + " z faktury o id " + negativeInvoice.getId());
            saveInvoice(invoice, newAmount, false);
        }

        if (invoiceAmount.compareTo(convertedNegativeAmount) == -1) {
            BigDecimal newNegativeInvoiceAmount = convertedNegativeAmount.subtract(invoiceAmount);
            BigDecimal newConvertedAmount = newNegativeInvoiceAmount.multiply(converter);
            saveInvoice(negativeInvoice, newConvertedAmount, false);

            invoice.setComment("Pomniejszono o -" + invoiceAmount + " z faktury o id " + negativeInvoice.getId());
            saveInvoice(invoice, zero, true);
        }
    }

    private void saveInvoice(Invoice invoice, BigDecimal newAmount, boolean isUsed) {
        invoice.setUsed(isUsed);
        invoice.setAmountToUse(newAmount);
        invoiceDao.save(invoice);
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
