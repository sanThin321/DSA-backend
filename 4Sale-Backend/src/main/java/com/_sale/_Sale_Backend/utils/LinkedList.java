package com._sale._Sale_Backend.utils;

// LinkedList.java
public class LinkedList<T> {

    // Inner class for Linked List Node
    private class Node {
        T data;
        Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;

    public LinkedList() {
        this.head = null;
        this.tail = null;
    }

    // Add an item at the end of the list
    public void add(T data) {
        Node newNode = new Node(data);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    // Remove and return the first item from the list (FIFO behavior)
    public T remove() {
        if (head == null) {
            return null;
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        return data;
    }

    // Check if the list is empty
    public boolean isEmpty() {
        return head == null;
    }

    // Peek at the first element without removing it
    public T peek() {
        if (head == null) {
            return null;
        }
        return head.data;
    }

    // Print all elements of the list for debugging
    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
}
