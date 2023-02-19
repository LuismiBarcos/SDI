package com.example.animals.business;

import com.example.animals.contracts.Animal;

/**
 * @author Luis Miguel Barcos
 */
public abstract class Mammal implements Animal {
    @Override
    public String animalClazz() {
        return "Mammal";
    }
}
