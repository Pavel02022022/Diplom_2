package org.practicum.order;

import org.practicum.model.Order;

import java.util.ArrayList;
import java.util.List;

import static org.practicum.utils.Utils.*;

public class OrderGenerator {

    public static Order randomOrder() {

        return new Order()
                .withIngredients(randomBun())
                .withIngredients(randomMain())
                .withIngredients(randomMain())
                .withIngredients(randomSauce());
    }

    public static Order orderWithoutIngridients() {

        return new Order();

    }
    public static Order orderWithWrongIngredient() {
        return new Order()
                .withIngredients("wrongIngredient61c0c5a71")
                .withIngredients("wrongIngredient61c0c5a76");
    }
}
