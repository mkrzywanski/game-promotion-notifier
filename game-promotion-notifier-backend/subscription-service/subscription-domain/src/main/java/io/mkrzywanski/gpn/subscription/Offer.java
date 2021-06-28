package io.mkrzywanski.gpn.subscription;

public class Offer {

    private String text;
    private int id;

    private Offer() {
    }

    public Offer(final String text, final int id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
