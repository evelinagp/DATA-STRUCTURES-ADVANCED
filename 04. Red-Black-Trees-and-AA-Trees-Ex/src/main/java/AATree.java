import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    public static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private int level;

        public Node(T value) {
            this.value = value;
            this.level = 1;
        }
    }

    private Node<T> root;

    public AATree() {

    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;
    }

    public void insert(T element) {
        this.root = this.insert(this.root, element);
    }

    private Node<T> insert(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }
        if (node.value.compareTo(element) < 0) {
            node.right = insert(node.right, element);
        } else {
            node.left = insert(node.left, element);
        }
        node = skew(node);
        node = split(node);

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right != null && node.right.level == node.level
                && node.right.right != null && node.right.right.level == node.level) {
            node = rotateLeft(node);
            node.level++;
            return node;
        }
        return node;
    }


    private Node<T> skew(Node<T> node) {
        if (node.left != null && node.left.level == node.level) {
            return rotateRight(node);
        }
        return node;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }

    public int countNodes() {
        return this.countChildren(this.root);
    }

    private int countChildren(Node<T> node) {
        if (node == null) return 0;

        return this.countChildren(node.left) + this.countChildren(node.right) + 1;
    }

    public boolean search(T element) {
        return this.search(this.root, element);
    }

    private boolean search(Node<T> node, T element) {
        if (node == null) return false;
        if (node.value.compareTo(element) < 0) {
            return search(node.right, element);
        } else if(node.value.compareTo(element) > 0) {
            return search(node.left, element);
        }
        return true;
    }

    public void inOrder(Consumer<T> consumer) {
        consumerInOrder(this.root, consumer);
    }

    private void consumerInOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) return;

        consumerInOrder(node.left, consumer);
        consumer.accept(node.value);
        consumerInOrder(node.right, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        consumerPreOrder(this.root, consumer);
    }

    private void consumerPreOrder(Node<T> node, Consumer<T> consumer) {

        if (node == null) return;

        consumer.accept(node.value);
        consumerPreOrder(node.left, consumer);
        consumerPreOrder(node.right, consumer);

    }

    public void postOrder(Consumer<T> consumer) {
        consumerPostOrder(this.root, consumer);
    }

    private void consumerPostOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) return;

        consumerPostOrder(node.left, consumer);
        consumerPostOrder(node.right, consumer);
        consumer.accept(node.value);
    }
}