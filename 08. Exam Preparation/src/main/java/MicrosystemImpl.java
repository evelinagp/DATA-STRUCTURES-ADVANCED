
import java.util.*;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {

    private Map<Integer, Computer> computersByNumber;

    //private Map<Integer, List<Computer>> storeWithComputers;
    public MicrosystemImpl() {
        this.computersByNumber = new HashMap<>();
    }

    @Override
    public void createComputer(Computer computer) {
        if (contains(computer.getNumber())) {
            throw new IllegalArgumentException();
        }
        this.computersByNumber.put(computer.getNumber(), computer);
    }

    @Override
    public boolean contains(int number) {
        return this.computersByNumber.containsKey(number);
    }

    @Override
    public int count() {
        return this.computersByNumber.size();
    }

    // Faster way
    @Override
    public Computer getComputer(int number) {
        if (!contains(number)) {
            throw new IllegalArgumentException();
        }
        return this.computersByNumber.get(number);
    }
    //Slower way????
//        Computer computer = this.computersByNumber.get(number);
//        if (computer == null) {
//            throw new IllegalArgumentException();
//        }
//        return computer;
//    }


    @Override
    public void remove(int number) {
        Computer computer = this.getComputer(number);
        if (computer == null) {
            throw new IllegalArgumentException();
        }
        this.computersByNumber.remove(number);
    }

    @Override
    public void removeWithBrand(Brand brand) {
        int oldSize = this.count();
        this.computersByNumber = this.computersByNumber.entrySet().stream()
                .filter(c -> c.getValue().getBrand().compareTo(brand) != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int newSize = this.count();
        if (oldSize == newSize) {
            throw new IllegalArgumentException();
        }
    }
    //Slow!!!
//        List<Computer> computersByBrand = this.computersByNumber.values().stream().filter(c -> c.getBrand().equals(brand)).collect(Collectors.toList());
//        if (computersByBrand.isEmpty()) {
//            throw new IllegalArgumentException();
//        }
//        computersByNumber.values().removeAll(computersByBrand);


    @Override
    public void upgradeRam(int ram, int number) {
        Computer computer = this.getComputer(number);
        if (ram > computer.getRAM()) {
            computer.setRAM(ram);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        return this.computersByNumber.values().stream().filter(c -> c.getBrand().compareTo(brand) == 0)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return this.computersByNumber.values().stream().filter(c -> Double.compare(c.getScreenSize(), screenSize) == 0)
                .sorted((c1, c2) -> Integer.compare(c2.getNumber(), c1.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return this.computersByNumber.values().stream().filter(c -> c.getColor().compareTo(color) == 0)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return this.computersByNumber.values().stream().filter(c -> c.getPrice() >= minPrice && c.getPrice() <= maxPrice)
                .sorted((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()))
                .collect(Collectors.toList());
    }
}
