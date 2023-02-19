package com.example.animals.business.services;

import com.example.animals.business.Aves;
import org.sdi.annotations.Component;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = "com.example.animals.contracts.Animal")
public class Chicken extends Aves {
    @Override
    public String sound() {
        return "Chicken sound";
    }
}
