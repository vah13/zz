/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.system.control.startup;

import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageConversion {
    private static final int MASK = 1;
    private static final int ANTI_MASK = -2;
    private static final Random ra = new Random();

    private ImageConversion() {
    }

    public static BufferedImage createRandomImage() {
        int width = 120;
        int height = 120;
        BufferedImage img = new BufferedImage(width, height, 1);
        int y = 0;
        while (y < height) {
            for (int x = 0; x < width; ++x) {
                int r = (int)(ra.nextDouble() * 256.0);
                int g = (int)(ra.nextDouble() * 256.0);
                int b = (int)(ra.nextDouble() * 256.0);
                int rgb = 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
                img.setRGB(x, y, rgb);
            }
            ++y;
        }
        return img;
    }

    public static BufferedImage write(BufferedImage image, String t) {
        int x = 0;
        int y = 0;
        int i = 0;
        while (i < t.length()) {
            int b = t.charAt(i);
            for (int j = 0; j < 8; b >>= 1, ++j) {
                int flag = b & '\u0001';
                if (flag == 1) {
                    if (x < image.getWidth()) {
                        image.setRGB(x, y, image.getRGB(x, y) | 1);
                        ++x;
                        continue;
                    }
                    x = 0;
                    image.setRGB(x, ++y, image.getRGB(x, y) | 1);
                    ++x;
                    continue;
                }
                if (x < image.getWidth()) {
                    image.setRGB(x, y, image.getRGB(x, y) & 0xFFFFFFFE);
                    ++x;
                    continue;
                }
                x = 0;
                image.setRGB(x, ++y, image.getRGB(x, y) & 0xFFFFFFFE);
                ++x;
            }
            ++i;
        }
        return image;
    }

    public static String read(BufferedImage image, int length) {
        int x = 0;
        int y = 0;
        char[] c = new char[length];
        int i = 0;
        while (i < length) {
            int b = 0;
            for (int j = 0; j < 8; ++j) {
                int flag;
                if (x < image.getWidth()) {
                    flag = image.getRGB(x, y) & 1;
                    ++x;
                } else {
                    x = 0;
                    flag = image.getRGB(0, ++y) & 1;
                    ++x;
                }
                if (flag == 1) {
                    b >>= 1;
                    b |= 0x80;
                    continue;
                }
                b >>= 1;
            }
            c[i] = (char)b;
            ++i;
        }
        return new String(c);
    }
}
