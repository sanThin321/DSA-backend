package com._sale._Sale_Backend.utils;

public class CustomHashMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;  // Load factor threshold for resizing
    private int size;  // Number of key-value pairs in the map
    private int capacity;  // Current capacity of the bucket array
    private CustomLinkedList<Node<K, V>>[] buckets;

    // Constructor to initialize buckets
    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        this.capacity = 16; 
        this.buckets = new CustomLinkedList[capacity];
        this.size = 0; 
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new CustomLinkedList<>();
        }
    }

    // Inner class representing key-value pairs
    private static class Node<K, V> {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Hash function to compute the bucket index
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    // Method to add or update a key-value pair
    public void put(K key, V value) {
        // Resize the buckets array if needed
        if ((float) size / capacity >= LOAD_FACTOR) {
            resize();
        }

        int index = getBucketIndex(key);
        CustomLinkedList<Node<K, V>> bucket = buckets[index];

        // Check if the key already exists; update its value if found
        for (Node<K, V> Node : bucket) {
            if (Node.key.equals(key)) {
                Node.value = value;  // Update value
                return;
            }
        }

        // If key not found, add a new Node
        bucket.add(new Node<>(key, value));
        size++;  // Increment size since a new Node is added
    }

    // Method to get a value by key
    public V get(K key) {
        int index = getBucketIndex(key);
        CustomLinkedList<Node<K, V>> bucket = buckets[index];

        // Search for the key in the bucket
        for (Node<K, V> Node : bucket) {
            if (Node.key.equals(key)) {
                return Node.value;
            }
        }
        return null;  // Return null if key not found
    }

    // Method to remove a key-value pair
    public void remove(K key) {
        int index = getBucketIndex(key);
        CustomLinkedList<Node<K, V>> bucket = buckets[index];

        // Find and remove the Node with the given key
        boolean removed = bucket.removeIf(Node -> Node.key.equals(key));
        if (removed) {
            size--;  // Decrement size if the Node was removed
        }
    }

    // Method to check if the map contains a key
    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        CustomLinkedList<Node<K, V>> bucket = buckets[index];

        for (Node<K, V> Node : bucket) {
            if (Node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    // Resize the buckets array when the load factor exceeds the threshold
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        CustomLinkedList<Node<K, V>>[] newBuckets = new CustomLinkedList[newCapacity];

        // Initialize the new buckets
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new CustomLinkedList<>();
        }

        // Rehash all existing entries and put them into the new buckets
        for (CustomLinkedList<Node<K, V>> bucket : buckets) {
            for (Node<K, V> Node : bucket) {
                int newIndex = Math.abs(Node.key.hashCode() % newCapacity);
                newBuckets[newIndex].add(Node);
            }
        }

        // Replace the old buckets array with the new one
        buckets = newBuckets;
        capacity = newCapacity;
    }
}
