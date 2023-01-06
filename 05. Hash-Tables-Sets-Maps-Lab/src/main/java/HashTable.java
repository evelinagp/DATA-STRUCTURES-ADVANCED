import java.util.*;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {

    public static final int INITIAL_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.80d;

    private LinkedList<KeyValue<K, V>>[] slots;

    private int count;
    private int capacity;

    public HashTable() {
        this(INITIAL_CAPACITY);

    }

    public HashTable(int capacity) {
        this.slots = new LinkedList[capacity];
        this.count = 0;
        this.capacity = capacity;
    }

    public void add(K key, V value) {
        this.growIfNeeded();
        int index = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> linkedList = this.slots[index];

        if (linkedList == null) {
            linkedList = new LinkedList<>();
        }
        for (KeyValue<K, V> current : linkedList) {
            if (current.getKey().equals(key)) {
                throw new IllegalArgumentException("Key already exists" + key);
            }
        }
        KeyValue<K, V> toInsert = new KeyValue<>(key, value);
        linkedList.add(toInsert);

        this.slots[index] = linkedList;
        this.count++;
    }

    private int findSlotNumber(K key) {
        return Math.abs(key.hashCode() % this.capacity);
    }

    private void growIfNeeded() {
        if ((double) (this.size() + 1) / this.capacity() > LOAD_FACTOR) {
            this.grow();
        }
    }

    private void grow() {
        HashTable<K, V> newHashTable = new HashTable<>(2 * this.capacity);
        for (LinkedList<KeyValue<K, V>> element : slots) {
            if (element != null) {
                for (KeyValue<K, V> keyValue : element) {
                    newHashTable.add(keyValue.getKey(), keyValue.getValue());
                }
            }
        }
        this.slots = newHashTable.slots;
        this.count = newHashTable.count;
        this.capacity *= 2;
    }

    public int size() {
        return this.count;
    }

    public int capacity() {
        return this.slots.length;
    }

    public boolean addOrReplace(K key, V value) {
        this.growIfNeeded();
        int index = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> linkedList = this.slots[index];

        if (linkedList == null) {
            linkedList = new LinkedList<>();
        }
        boolean updated = false;

        for (KeyValue<K, V> current : linkedList) {
            if (current.getKey().equals(key)) {
                current.setValue(value);
                updated = true;
            }
        }
        if (!updated) {
            KeyValue<K, V> toInsert = new KeyValue<>(key, value);
            linkedList.add(toInsert);
            this.count++;
        }

        this.slots[index] = linkedList;
        return !updated;
    }

    public V get(K key) {
        KeyValue<K, V> element = find(key);
        if (element == null) {
            throw new IllegalArgumentException();
        }
        return element.getValue();
    }

    public KeyValue<K, V> find(K key) {
        int slotNumber = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> linkedList = this.slots[slotNumber];

        if (linkedList != null) {
            for (KeyValue<K, V> element : linkedList) {
                if (element.getKey().equals(key)) {
                    return element;
                }
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return this.find(key) != null;
    }

    public boolean remove(K key) {
        int slotNumber = findSlotNumber(key);
        LinkedList<KeyValue<K, V>> slotsList = this.slots[slotNumber];

        if (slotsList == null) {
            return false;
        }
        KeyValue<K, V> toRemove = null;

        for (KeyValue<K, V> pair : slotsList) {
            if (pair.getKey().equals(key)) {
                toRemove = pair;
                break;
            }
        }
        boolean result = toRemove != null && slotsList.remove(toRemove);

        if (result) {
            this.count--;
        }

        return result;
    }

    public void clear() {
        this.capacity = INITIAL_CAPACITY;
        this.slots = new LinkedList[this.capacity];

        this.count = 0;

    }

    public Iterable<K> keys() {
        List<K> keys = new ArrayList<>();
        for (KeyValue<K, V> pair : this) {
            keys.add(pair.getKey());
        }
        return keys;
    }

    public Iterable<V> values() {
        List<V> values = new ArrayList<>();
        for (KeyValue<K, V> pair : this) {
            values.add(pair.getValue());
        }
        return values;
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        return new HashIterator();
    }

    private class HashIterator implements Iterator<KeyValue<K, V>> {
        Deque<KeyValue<K, V>> elements;

        public HashIterator() {
            this.elements = new ArrayDeque<>();

            for (LinkedList<KeyValue<K, V>> slot : slots) {
                if (slot != null) {
                    elements.addAll(slot);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !elements.isEmpty();
        }

        @Override
        public KeyValue<K, V> next() {
            if (!hasNext()) {
                throw new IllegalArgumentException("Empty hash table!");
            }
            return elements.poll();
        }
    }
}
