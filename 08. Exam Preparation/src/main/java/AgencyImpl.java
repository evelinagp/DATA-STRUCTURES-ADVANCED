import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgencyImpl implements Agency {

    private Map<String, Invoice> invoicesByNumber;
    private Map<LocalDate, Map<String, Invoice>> invoicesByDueDates;
    private Set<Invoice> paid;

    public AgencyImpl() {
        this.invoicesByNumber = new HashMap<>();
        this.invoicesByDueDates = new HashMap<>();
        this.paid = new HashSet<>();
    }

    @Override
    public void create(Invoice invoice) {
        if (contains(invoice.getNumber())) {
            throw new IllegalArgumentException();
        }
        this.invoicesByNumber.put(invoice.getNumber(), invoice);
        Map<String, Invoice> currentByDate = this.invoicesByDueDates.get(invoice.getDueDate());
        if (currentByDate == null) {
            currentByDate = new HashMap<>();
        }
        currentByDate.put(invoice.getNumber(), invoice);
        this.invoicesByDueDates.put(invoice.getDueDate(), currentByDate);
    }

    @Override
    public boolean contains(String number) {
        return this.invoicesByNumber.containsKey(number);
    }

    @Override
    public int count() {
        return this.invoicesByNumber.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        if (!this.invoicesByDueDates.containsKey(dueDate)) {
            throw new IllegalArgumentException();
        }
        Map<String, Invoice> currentInvoicesByDate = this.invoicesByDueDates.get(dueDate);
        currentInvoicesByDate.forEach((k, v) -> {
            v.setSubtotal(0);
            this.paid.add(v);
            this.invoicesByNumber.put(k, v);
        });
        this.invoicesByDueDates.put(dueDate, currentInvoicesByDate);
    }


    @Override
    public void throwInvoice(String number) {
        Invoice invoice = invoicesByNumber.get(number);
        if (invoice == null) {
            throw new IllegalArgumentException();
        }
        this.invoicesByNumber.remove(invoice.getNumber());
        this.invoicesByDueDates.get(invoice.getDueDate()).remove(invoice.getNumber());

    }

    @Override
    public void throwPayed() {
        this.paid.forEach(i -> {
            this.invoicesByNumber.remove(i.getNumber());
            this.invoicesByDueDates.get(i.getDueDate()).remove(i.getNumber());
        });
        this.paid.clear();
    }

    @Override
    //todo check if works correctly
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        return this.invoicesByNumber.values().stream().filter(i -> i.getIssueDate().minusDays(1).isAfter(startDate)
                        && i.getIssueDate().plusDays(1).isBefore(endDate))
                .sorted(Comparator.comparing(Invoice::getIssueDate)
                        .thenComparing(Invoice::getDueDate))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {
        List<Invoice> searched = this.invoicesByNumber.values().
                stream().filter(i -> i.getNumber().contains(number))
                .collect(Collectors.toUnmodifiableList());
        if (searched.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return searched;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoiceList = this.invoicesByNumber.values().stream().filter(i -> i.getDueDate().isAfter(startDate)
                        && i.getDueDate().isBefore(endDate))
                .collect(Collectors.toUnmodifiableList());
        if (invoiceList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        invoiceList.forEach(i -> {
            this.invoicesByNumber.remove(i.getNumber());
            this.invoicesByDueDates.get(i.getDueDate()).remove(i.getNumber());
        });

        return invoiceList;
    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return this.invoicesByNumber.values().stream().filter(i -> i.getDepartment().compareTo(department) == 0)
                .sorted((i1, i2) -> {
                    int result = Double.compare(i2.getSubtotal(), i1.getSubtotal());
                    if (result == 0) {
                        result = i1.getIssueDate().compareTo(i2.getIssueDate());
                    }
                    return result;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return this.invoicesByNumber.values().stream().filter(i -> i.getCompanyName().compareTo(companyName) == 0)
                .sorted((i1, i2) -> i2.getNumber().compareTo(i1.getNumber()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        if (!this.invoicesByDueDates.containsKey(endDate)) {
            throw new IllegalArgumentException();
        }
        invoicesByDueDates.get(endDate).values().forEach(i -> i.setDueDate(i.getDueDate().plusDays(days)));
    }
}
