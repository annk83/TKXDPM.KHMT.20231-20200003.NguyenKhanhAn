package annk.aims.domain;

import lombok.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class CartItem {
    private int itemId;
    private String name;
    private BufferedImage image;
    private int count;
    private double price;
}
