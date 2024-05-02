package com.example.animals;

import com.example.animals.contracts.Animal;
import org.sdi.domain.annotations.Component;
import org.sdi.domain.annotations.Inject;

/**
 * @author Luis Miguel Barcos
 */
@Component(classes = AnimalsClient.class)
public class AnimalsClient {

    @Inject
    private Animal animal;

    @Inject("com.example.animals.business.services.Dog")
    private Animal dog;
    @Inject("com.example.animals.business.services.Chicken")
    private Animal chicken;
    @Inject("com.example.animals.business.services.Cat")
    private Animal cat;

    public void sounds() {
        System.out.println("First random animal sound\n" + animal.sound() + "\nClass: "+ animal.animalClazz());
        System.out.println("****************************");
        System.out.println("First random animal sound\n" + dog.sound() + "\nClass: "+ dog.animalClazz());
        System.out.println("****************************");
        System.out.println("First random animal sound\n" + chicken.sound() + "\nClass: "+ chicken.animalClazz());
        System.out.println("****************************");
        System.out.println("First random animal sound\n" + cat.sound() + "\nClass: "+ cat.animalClazz());
        System.out.println("****************************");
    }
}
