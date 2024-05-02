package com.example.animals.business.services;

import com.example.animals.business.Mammal;
import com.example.animals.contracts.Animal;
import org.sdi.domain.annotations.Component;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = Animal.class)
public class Dog extends Mammal {
    @Override
    public String sound() {
        return "Dog sound";
    }
}
