public class AvlTree<T extends Comparable<T>> implements Tree<T> {
    private Node<T> root;

    private int height(Node<T> node) {
        if (node == null) {
            return -1;
        }

        return node.getHeight();
    }

    private int getBalance(Node<T> node) {
        if (node == null) {
            return 0;
        }

        return height(node.getLeftChild()) - height(node.getRightChild());
    }

    @Override
    public void insert(T data) {
        root = insertNode(root, data);
    }

    private Node<T> insertNode(Node<T> node, T data) {
        if (node == null) {
            return new Node<T>(data);
        }

        if (data.compareTo(node.getData()) < 0) {
            node.setLeftChild(insertNode(node.getLeftChild(), data));
        } else {
            node.setRightChild(insertNode(node.getRightChild(), data));
        }

        node.setHeight(Math.max(height(node.getLeftChild()), height(node.getRightChild())) + 1);

        return settleViolation(node, data);
    }

    private Node<T> settleViolation(Node<T> node, T data) {
        int balance = getBalance(node);

        if (balance > 1 && data.compareTo(node.getLeftChild().getData()) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && data.compareTo(node.getRightChild().getData()) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && data.compareTo(node.getLeftChild().getData()) > 0) {
            node.setLeftChild(rotateLeft(node.getLeftChild()));
            return rotateRight(node);
        }

        if (balance < -1 && data.compareTo(node.getRightChild().getData()) < 0) {
            node.setRightChild(rotateRight(node.getRightChild()));
            return rotateLeft(node);
        }

        return node;
    }

    private Node<T> rotateRight(Node<T> node) {
        System.out.println("Rotating right on node: " + node);

        Node<T> tmpLeftNode = node.getLeftChild();
        Node<T> tmpRightNode = tmpLeftNode.getRightChild();

        tmpLeftNode.setRightChild(node);
        node.setLeftChild(tmpRightNode);

        node.setHeight(Math.max(height(node.getLeftChild()), height(node.getRightChild())) + 1);
        tmpLeftNode.setHeight(Math.max(height(tmpLeftNode.getLeftChild()), height(tmpLeftNode.getRightChild())) + 1);

        return tmpLeftNode;
    }

    private Node<T> rotateLeft(Node<T> node) {
        System.out.println("Rotating left on node: " + node);

        Node<T> tmpRightNode = node.getRightChild();
        Node<T> tmpLeftNode = tmpRightNode.getLeftChild();

        tmpRightNode.setLeftChild(node);
        node.setRightChild(tmpLeftNode);

        node.setHeight(Math.max(height(node.getLeftChild()), height(node.getRightChild())) + 1);
        tmpRightNode.setHeight(Math.max(height(tmpRightNode.getLeftChild()), height(tmpRightNode.getRightChild())) + 1);

        return tmpRightNode;
    }

    @Override
    public void remove(T data) {
        root = removeNode(root, data);
    }

    private Node<T> removeNode(Node<T> node, T data) {
        if (node == null) {
            return node;
        }

        if (data.compareTo(node.getData()) < 0) {
            node.setLeftChild(removeNode(node.getLeftChild(), data));
        }
        else if (data.compareTo(node.getData()) > 0) {
            node.setRightChild(removeNode(node.getRightChild(), data));
        }
        else {
            if (node.getLeftChild() == null && node.getRightChild() == null) {
                System.out.println("Removing a leaf node...");
                return null;
            }

            if (node.getLeftChild() == null) {
                System.out.println("Removing the right child...");

                Node<T> tmpRightNode = node.getRightChild();
                node = null;
                return tmpRightNode;
            } else if (node.getRightChild() == null) {
                System.out.println("Removing the left child...");

                Node<T> tmpLeftNode = node.getLeftChild();
                node = null;
                return tmpLeftNode;
            }

            System.out.println("Removing item with two children...");
            Node<T> tmpPredecessorNode = getPredecessor(node.getLeftChild());

            node.setData(tmpPredecessorNode.getData());
            node.setLeftChild(removeNode(node.getLeftChild(), tmpPredecessorNode.getData()));
        }

        node.setHeight(Math.max(height(node.getLeftChild()), height(node.getRightChild())) + 1);

        return settleDeletion(node);
    }

    private Node<T> getPredecessor(Node<T> node) {
        Node<T> predecessor = node;

        while (predecessor.getRightChild() != null) {
            predecessor = predecessor.getRightChild();
        }

        return predecessor;
    }

    private Node<T> settleDeletion(Node<T> node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.getLeftChild()) < 0) {
                node.setLeftChild(rotateLeft(node.getLeftChild()));
            }

            return rotateRight(node);
        }


        if (balance < -1) {
            if (getBalance(node.getRightChild()) > 0) {
                node.setRightChild(rotateRight(node.getRightChild()));
            }

            return rotateLeft(node);
        }

        return node;
    }

    @Override
    public void traverse() {
        traverseNode(root);
    }

    private void traverseNode(Node<T> node) {
        if (node == null) {
            return;
        }

        traverseNode(node.getLeftChild());
        System.out.print(node + " ");
        traverseNode(node.getRightChild());
    }
}