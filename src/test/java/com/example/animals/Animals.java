package com.example.animals;

import org.sdi.injector.SimpleDependencyInjector;

/**
 * @author Luis Miguel Barcos
 */
public class Animals {
    public static void main(String[] args) {
        SimpleDependencyInjector simpleDependencyInjector = new SimpleDependencyInjector();
        simpleDependencyInjector.init(Animals.class);

        AnimalsClient service = simpleDependencyInjector.getService(AnimalsClient.class);
        service.sounds();

    }
}
