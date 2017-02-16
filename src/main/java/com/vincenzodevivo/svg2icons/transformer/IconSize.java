package com.vincenzodevivo.svg2icons.transformer;

/**
 * Created by Vincenzo De Vivo on 15/02/2017.
 */
public class IconSize {
    public final static String SEPARATOR = "x";
    private Float width;
    private Float height;

    public IconSize(String stringSize) {
        String[] values = stringSize.split("x");
        if (values.length != 2) {
            throw new RuntimeException("Malformed string size, expect format like '16x16', actual: " + stringSize);
        }
        this.width = Float.parseFloat(values[0]);
        this.height = Float.parseFloat(values[1]);
    }

    public IconSize(Object width, Object height) {
        this.width = Float.parseFloat(width.toString());
        this.height = Float.parseFloat(height.toString());
    }

    public IconSize(Float width, Float height) {
        this.width = width;
        this.height = height;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return width.intValue() + SEPARATOR + height.intValue();
    }
}
