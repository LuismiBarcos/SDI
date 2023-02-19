package com.example.animals.business.services;

import com.example.animals.business.Aves;
import com.example.animals.contracts.Animal;
import org.sdi.annotations.Component;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = Animal.class)
public class Parrot extends Aves {
    @Override
    public String sound() {
        return "Parrot sound";
    }
}
