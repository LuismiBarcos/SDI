package com.example.animals.business.services;

import com.example.animals.business.Mammal;
import org.sdi.annotations.Component;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = "com.example.animals.contracts.Animal")
public class Cat extends Mammal {

    @Override
    public String sound() {
        return "Cat sound";
    }
}