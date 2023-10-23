package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nutrition")
public class Nutrition {

    @Id
    @Column(name = "食品名")
    private String foodName;
    @Column(name = "エネルギー(kcal)")
    private double energy;
    @Column(name = "たんぱく質")
    private double protein;
    @Column(name = "脂質")
    private double fat;
    @Column(name = "コレステロール")
    private double cholesterol;
    @Column(name = "炭水化物")
    private double carbohydrates;
    @Column(name = "username")
    private String username;
    @Column(name = "date")
    private double date;
}