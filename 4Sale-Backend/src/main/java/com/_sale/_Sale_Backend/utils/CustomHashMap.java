package com._sale._Sale_Backend.utils;

public class CustomHashMap<K, V> {
    private static final int SIZE = 16;  // Default size of the map
    private CustomLinkedList<Entry<K, V>>[] buckets;

    // Constructor to initialize buckets
    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        buckets = new CustomLinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            buckets[i] = new CustomLinkedList<>();
        }
    }

    // Inner class representing key-value pairs
    private static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Hash function to compute the bucket index
    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % SIZE);
    }

    // Method to add or update a key-value pair
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        CustomLinkedList<Entry<K, V>> bucket = buckets[index];

        // Check if the key already exists; update its value if found
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;  // Update value
                return;
            }
        }

        // If key not found, add a new entry
        bucket.add(new Entry<>(key, value));
    }

    // Method to get a value by key
    public V get(K key) {
        int index = getBucketIndex(key);
        CustomLinkedList<Entry<K, V>> bucket = buckets[index];

        // Search for the key in the bucket
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;  // Return null if key not found
    }

    // Method to remove a key-value pair
    public void remove(K key) {
        int index = getBucketIndex(key);
        CustomLinkedList<Entry<K, V>> bucket = buckets[index];

        // Find and remove the entry with the given key
        bucket.removeIf(entry -> entry.key.equals(key));
    }

    // Method to check if the map contains a key
    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        CustomLinkedList<Entry<K, V>> bucket = buckets[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }
}
