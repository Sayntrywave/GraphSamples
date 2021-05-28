package ru.vsu.cs.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;


public class DrawUtils {
    /**
     * Рисование строки в центре прямоугольника (x, y, width, height).
     * @param gr Экземпляр <code>Graphics</code> для рисования
     * @param font Шрифт для рисования
     * @param s Строка, которую надо нарисовать
     * @param x Координата x верхнего левого угла прямоугольника
     * @param y Координата y верхнего левого угла прямоугольника
     * @param width Ширина прямоугольника
     * @param height Высота прямоугольника
     * @see java.awt.Graphics
     */
    public static void drawStringInCenter(Graphics gr, Font font, String s, int x, int y, int width, int height) {
        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D r2D = font.getStringBounds(s, frc);
        int rWidth = (int) Math.round(r2D.getWidth());
        int rHeight = (int) Math.round(r2D.getHeight());
        int rX = (int) Math.round(r2D.getX());
        int rY = (int) Math.round(r2D.getY());

        int a = (width / 2) - (rWidth / 2) - rX;
        int b = (height / 2) - (rHeight / 2) - rY;

        gr.setFont(font);
        gr.drawString(s, x + a, y + b);
    }

    /**
     * Рисование строки в центре прямоугольника (x, y, width, height).
     * @param gr Экземпляр <code>Graphics</code> для рисования
     * @param font Шрифт для рисования
     * @param s Строка, которую надо нарисовать
     * @param r Прямоугольник
     * @see java.awt.Graphics
     * @see java.awt.Rectangle
     */
    public static void drawStringInCenter(Graphics gr, Font font, String s, Rectangle r) {
        drawStringInCenter(gr, font, s, r.x, r.y, r.width, r.height);
    }


    /**
     * Возвращает контрастный цвет (белый или черный) к переданному цвету
     * @param color Цвет
     * @return Контрастный цвет
     */
    public static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }
}
