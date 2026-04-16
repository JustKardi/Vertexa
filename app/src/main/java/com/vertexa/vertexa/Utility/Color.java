package com.vertexa.vertexa.Utility;

public class Color {

    private float r, g, b, a;

    private float rgbaArr[];

    public Color(float r, float g, float b, float a) {

        this.r = bound(r);
        this.g = bound(g);
        this.b = bound(b);
        this.a = bound(a);

        rgbaArr = new float[] {
            r, g, b, a
        };

    }

    private float bound(float x) {
        return Math.max(0.0f, Math.min(1.0f, x));
    }

    public Color getRGBA() {
        return this;
    }

    public String getRBAString() {
        return String.format("%.2f, %.2f, %.2f, %.2f", r, g, b, a);
    }

    public float[] getColorArr() {
        return rgbaArr;
    }

    public String getHex() {
        return String.format("#%02X%02X%02X", (int)(r*255), (int)(g*255), (int)(b*255));
    }

    public float getSingleColor(char x) {
        switch (x) {
            case 'r' -> {
                return r;
            }
            case 'g' -> {
                return g;
            }
            case 'b' -> {
                return b;
            }
            default -> throw new IllegalArgumentException("Entered value is not an accepted parameter.");
        }
    }
}