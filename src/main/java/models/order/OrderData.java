package models.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrderData {
    @Getter @Setter
    private List<String> ingredients;


    public OrderData(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderData() {}


    @Override
    public String toString() {
        return "OrderData{" +
                "ingredients=" + ingredients +
                '}';
    }

}
