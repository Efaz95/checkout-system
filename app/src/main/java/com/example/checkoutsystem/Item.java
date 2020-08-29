package com.example.checkoutsystem;

public class Item {
    String itemAvailability;
    String itemName;

    public Item(String itemAvailability, String itemName) {
        this.itemName = itemName;
        this.itemAvailability = itemAvailability;
    }

    public Item() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAvailability() {
        return itemAvailability;
    }

    public void setItemAvailability(String itemAvailability) {
        this.itemAvailability = itemAvailability;
    }
}
