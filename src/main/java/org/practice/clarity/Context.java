package org.practice.clarity;

public class Context {
    public void handle(Products products) {
        switch (products) {
            case FLIGHTS -> {
                System.out.println("Flights");
            }
            case HOTELS -> {
                System.out.println("Hotels");
            }
            default -> {
                System.out.println("Unknown");
            }
        }
    }
}
