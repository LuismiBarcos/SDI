package com.example.animals.business.services;

import com.example.animals.business.Mammal;
import com.example.animals.contracts.Animal;
import org.sdi.annotations.Component;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = Animal.class)
public class Cat extends Mammal {

    @Override
    public String sound() {
        return "Cat sound";
    }
}