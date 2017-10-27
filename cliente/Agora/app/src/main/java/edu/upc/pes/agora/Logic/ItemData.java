package edu.upc.pes.agora.Logic;

/**
 * Created by carlo on 24/10/2017.
 */

public class ItemData {

    String text;
    Integer imageId;

    public ItemData(String text, Integer imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public Integer getImageId() {
        return imageId;
    }
}
