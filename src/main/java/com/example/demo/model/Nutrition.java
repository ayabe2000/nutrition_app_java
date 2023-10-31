package com.example.demo.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;



@Getter
@Setter
@Entity
@Table(name = "nutrition")
public class Nutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "食品名")
    private String foodName;
    @Column(name = "エネルギー(kcal)")
    private Double energy = 0.0;
    @Column(name = "たんぱく質")
    private Double protein= 0.0;
    @Column(name = "脂質")
    private Double fat = 0.0;
    @Column(name = "コレステロール")
    private Double cholesterol = 0.0;
    @Column(name = "炭水化物")
    private Double carbohydrates = 0.0;


}
