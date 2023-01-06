import java.util.*;
import java.util.stream.Collectors;

public class PersonCollectionImpl implements PersonCollection {

    Map<String, Person> peopleByEmail = new TreeMap<>();

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (peopleByEmail.containsKey(email)) {
            return false;
        }
        peopleByEmail.put(email, new Person(email, name, age, town));
        return true;
    }

    @Override
    public int getCount() {
        return this.peopleByEmail.size();
    }

    @Override
    public boolean delete(String email) {
        if (!peopleByEmail.containsKey(email)) {
            return false;
        }
        peopleByEmail.remove(email);
        return true;
    }

    @Override
    public Person find(String email) {
        if (this.peopleByEmail.containsKey(email)) {
            return this.peopleByEmail.get(email);
        }
        return null;
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        List<Person> sortedEmails = new ArrayList<>();
        for (Person person : peopleByEmail.values()) {
            int index = person.getEmail().indexOf("@") + 1;
            String domain = person.getEmail().substring(index);
            if (domain.equals(emailDomain)) {
                sortedEmails.add(person);
            }
        }
        Collections.sort(sortedEmails);
        return sortedEmails;
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        List<Person> peopleList = new ArrayList<>();
        for (Person person : peopleByEmail.values()) {
            if (person.getName().equals(name) && person.getTown().equals(town)) {
                peopleList.add(person);
            }
        }
        Collections.sort(peopleList);
        return peopleList;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return this.peopleByEmail.values().stream().filter(p -> p.getAge() >= startAge && p.getAge() <= endAge)
                .sorted(Comparator.comparingInt(Person::getAge)
                        .thenComparing(Person::getEmail)).collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        List<Person> filteredByTown = new ArrayList<>();
        findAll(startAge, endAge).forEach(p -> {
            boolean equals = p.getTown().equals(town);
            if (equals) {
                filteredByTown.add(p);
            }
        });
        return filteredByTown;
    }
}
