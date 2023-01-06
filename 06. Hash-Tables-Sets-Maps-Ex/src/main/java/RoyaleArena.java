import java.util.*;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {

    private Map<Integer, Battlecard> battleCardsById;
    private Map<CardType, Set<Battlecard>> arenaWithBattleCards;

    public RoyaleArena() {
        this.battleCardsById = new LinkedHashMap<>();
        this.arenaWithBattleCards = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        if (contains(card)) {
            throw new IllegalArgumentException("Card with id " + card.getId() + " has already exists!");
        }
        battleCardsById.putIfAbsent(card.getId(), card);
        arenaWithBattleCards.putIfAbsent(card.getType(), new TreeSet<>(Battlecard::compareTo));
        arenaWithBattleCards.get(card.getType()).add(card);
    }

    @Override
    public boolean contains(Battlecard card) {
        return this.battleCardsById.containsKey(card.getId());
    }

    @Override
    public int count() {
        return battleCardsById.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        Battlecard battlecard = battleCardsById.get(id);
        if (battlecard == null) {
            throw new IllegalArgumentException("Card with id " + id + " has not exists!");
        }
        battlecard.setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        Battlecard battlecard = battleCardsById.get(id);
        if (battlecard == null) {
            throw new UnsupportedOperationException("Card with id " + id + " has not exists!");
        }
        return battlecard;
    }

    @Override
    public void removeById(int id) {
        Battlecard battlecard = battleCardsById.get(id);
        if (battlecard == null) {
            throw new UnsupportedOperationException("Card with id " + id + " has not exists!");
        }
        this.battleCardsById.remove(id);
        this.arenaWithBattleCards.get(battlecard.getType()).remove(battlecard);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        Set<Battlecard> battlecards = getExistingCardsByType(type);
        return battlecards.stream().sorted(Battlecard::compareTo).collect(Collectors.toList());

    }

    private Set<Battlecard> getExistingCardsByType(CardType type) {
        Set<Battlecard> battlecards = this.arenaWithBattleCards.get(type);
        if (battlecards == null || battlecards.isEmpty()) {
            throw new UnsupportedOperationException("Cards with type " + type + " has not exists!");
        }
        return battlecards;
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        Set<Battlecard> battleCards = getExistingCardsByType(type);
        List<Battlecard> result = battleCards.stream().filter(c -> c.getDamage() > lo && c.getDamage() < hi)
                .sorted(Battlecard::compareTo)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException("Cards with between lo " + lo + " and hi damage " + hi + " has not exists!");

        }
        return result;
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> battleCards = getExistingCardsByType(type);
        List<Battlecard> result = battleCards.stream().filter(c -> c.getDamage() <= damage)
                .sorted(Battlecard::compareTo)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException("Cards with less or Equal damage " + damage + " has not exists!");
        }
        return result;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        return getExistingCardsByNameAndSwagDesc(name);
    }


    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        return getExistingCardsByNameAndSwagDesc(name).stream().filter(c -> c.getSwag() >= lo && c.getSwag() < hi)
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        Map<String, Battlecard> battleCardsByName = new LinkedHashMap<>();
        for (Battlecard battlecard : battleCardsById.values()) {
            if (!battleCardsByName.containsKey(battlecard.getName())) {
                battleCardsByName.put(battlecard.getName(), battlecard);
            } else {
                double oldSwag = battleCardsByName.get(battlecard.getName()).getSwag();
                double newSwag = battlecard.getSwag();
                if (newSwag > oldSwag) {
                    battleCardsByName.put(battlecard.getName(), battlecard);
                }
            }
            if (battleCardsByName.isEmpty()) {
                throw new UnsupportedOperationException("Cards has not exists!");
            }
        }
        return battleCardsByName.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n > this.count()) {
            throw new UnsupportedOperationException("Cards with index" + n + " has not exists!");
        }
        return this.battleCardsById.values().stream()
                .sorted(Comparator.comparingDouble(Battlecard::getSwag)
                        .thenComparingInt(Battlecard::getId))
                .limit(n)
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        return this.battleCardsById.values().stream().filter(c -> c.getSwag() >= lo && c.getSwag() <= hi)
                .sorted(Comparator.comparingDouble(Battlecard::getSwag))
                .collect(Collectors.toList());

    }

    @Override
    public Iterator<Battlecard> iterator() {
        return battleCardsById.values().iterator();
    }

    private List<Battlecard> getExistingCardsByNameAndSwagDesc(String name) {
        List<Battlecard> battlecards = this.battleCardsById.values().stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        if (battlecards.isEmpty()) {
            throw new UnsupportedOperationException("Cards with name " + name + " has not exists!");
        }
        return battlecards.stream().sorted(Comparator.comparingDouble(Battlecard::getSwag).reversed()
                        .thenComparingInt(Battlecard::getId))
                .collect(Collectors.toList());
    }
}
