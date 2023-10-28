package org.practice.clarity;

public class Exp<E extends Enum<E>> {
    E products;

    public Exp(E products) {
        this.products = products;
    }

    public String getElement() {
        return switch (products.name()) {
            case "FLIGHTS" -> "flights";
            case "HOTELS" -> "hotels";
            default -> null;
        };
    }
}
