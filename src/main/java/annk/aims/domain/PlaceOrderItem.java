package annk.aims.domain;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.util.Collection;

@Data
public class PlaceOrderItem {
    private int itemId;
    private String name;
    private BufferedImage image;
    private int count;
    private double price;
    private boolean rush;
    private double weight;
    private Collection<Integer> rushableProvince;
}
