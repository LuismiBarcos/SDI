package com.example.animals.business;

import com.example.animals.contracts.Animal;

/**
 * @author Luis Miguel Barcos
 */
public abstract class Aves implements Animal {
    @Override
    public String animalClazz() {
        return "Aves";
    }
}
