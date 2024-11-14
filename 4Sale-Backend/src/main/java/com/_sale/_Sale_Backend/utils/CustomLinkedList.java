package com._sale._Sale_Backend.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class CustomLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Method to add an element at the end (Append)
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {  // If list is empty
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;  // Add new node at the end
            tail = newNode;  // Update tail
        }
        size++;
    }

    // Method to remove elements based on a condition
    public boolean removeIf(Predicate<T> condition) {
        while (head != null && condition.test(head.data)) {
            head = head.next;
            size--;
        }

        if (head == null) {  // If list is empty after removals
            tail = null;
            return false;
        }

        Node<T> current = head;
        while (current != null && current.next != null) {
            if (condition.test(current.next.data)) {
                current.next = current.next.next;
                size--;
                if (current.next == null) {  // If we removed the last element
                    tail = current;  // Update tail
                }
            } else {
                current = current.next;
            }
        }
        return false;
    }

    // Method to get the size of the list
    public int size() {
        return size;
    }

    // Iterator implementation for easy traversal
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    // Method to display the elements of the list
    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}
