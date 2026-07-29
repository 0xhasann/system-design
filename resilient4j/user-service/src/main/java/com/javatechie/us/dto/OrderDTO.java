package com.javatechie.us.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OrderDTO {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    private int id;
    private String name;
    private String category;
    private String color;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private double price;

    public OrderDTO() {
    }

    public OrderDTO(int id, String name, String category,
            String color, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.color = color;
        this.price = price;
    }
}
