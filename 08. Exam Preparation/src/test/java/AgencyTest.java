import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AgencyTest {
    private Agency agency;

    @Before
    public void setup() {
        this.agency = new AgencyImpl();
    }

    @Test
    public void test_contains_with_correct_data() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        final boolean expectedContains = this.agency.contains(inv1.getNumber()) &&
                this.agency.contains(inv2.getNumber()) &&
                !this.agency.contains("5");

        assertTrue("Incorrect contains behavior", expectedContains);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_create_must_throw_exception() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("1",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);
    }

    @Test
    public void test_create_only_with_contains_check() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        final boolean expectedContains = this.agency.contains(inv1.getNumber()) &&
                this.agency.contains(inv2.getNumber()) &&
                !this.agency.contains("5");

        assertTrue("Incorrect contains behavior", expectedContains);
    }

    @Test
    public void test_create() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        final int expectedCount = 2;
        final int actualCount = this.agency.count();

        assertEquals("Incorrect count", expectedCount, actualCount);

        final boolean expectedContains = this.agency.contains(inv1.getNumber()) &&
                this.agency.contains(inv2.getNumber()) &&
                !this.agency.contains("5");

        assertTrue("Incorrect contains behavior", expectedContains);
    }

    @Test
    public void test_payInvoice() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        Assert.assertTrue(inv1.getSubtotal()== 125D);
        this.agency.payInvoice(LocalDate.of(2018, 3, 12));
        Assert.assertTrue(inv1.getSubtotal() == 0);

}
    @Test(expected = IllegalArgumentException.class)
    public void test_payInvoice_Should_Throw() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        Assert.assertTrue(inv1.getSubtotal()== 125D);
        assertEquals(1000D, inv2.getSubtotal(), 0.0);
        this.agency.payInvoice(LocalDate.of(2015, 3, 12));

    }
    @Test
    public void test_throwInvoice() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        assertEquals("Correct count", 2,  this.agency.count());
        this.agency.throwInvoice("1");
        assertEquals("Correct count", 1,  this.agency.count());


    }
    @Test(expected = IllegalArgumentException.class)
    public void test_throwInvoice_Should_Throw() {
        Invoice inv1 = new Invoice("1",
                "HRS",
                125D, Department.INCOMES,
                LocalDate.of(2018, 2, 12),
                LocalDate.of(2018, 3, 12));

        Invoice inv2 = new Invoice("2",
                "SoftUni",
                1000D, Department.INCOMES,
                LocalDate.of(2019, 2, 12),
                LocalDate.of(2019, 3, 12));

        this.agency.create(inv1);
        this.agency.create(inv2);

        assertEquals("Correct count", 2,  this.agency.count());
        this.agency.throwInvoice("3");
        assertEquals("Correct count", 2,  this.agency.count());

    }
//    @Test
//    public void test_throwPayed() {
//        Invoice inv1 = new Invoice("1",
//                "HRS",
//                0D, Department.INCOMES,
//                LocalDate.of(2018, 2, 12),
//                LocalDate.of(2018, 3, 12));
//
//        Invoice inv2 = new Invoice("2",
//                "SoftUni",
//                1000D, Department.INCOMES,
//                LocalDate.of(2019, 2, 12),
//                LocalDate.of(2019, 3, 12));
//
//        this.agency.create(inv1);
//        this.agency.create(inv2);
//
//        assertEquals("Correct count", 2,  this.agency.count());
//        this.agency.throwPayed();
//        assertEquals("Correct count", 1,  this.agency.count());
//
//
//    }
}

