package com.example.animals.business.services;

import com.example.animals.business.Mammal;
import org.sdi.annotations.Component;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = "com.example.animals.contracts.Animal")
public class Dog extends Mammal {
    @Override
    public String sound() {
        return "Dog sound";
    }
}
