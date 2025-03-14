import java.awt.Color;
import java.awt.Font;

public class Tile {
    private static final Color tileColor = new Color(15, 76, 129);
    private static final Color numberColor = new Color(31, 160, 239);
    private static final Color boxColor = new Color(31, 160, 239);
    private static final double lineThickness = 0.01;
    private static final Font numberFont = new Font("Arial", Font.BOLD, 50);
    private int number; // The number on the tile

    public Tile(int number) {
        this.number = number;
    }

    public void draw(int posX, int posY) {
        StdDraw.setPenColor(tileColor);
        StdDraw.filledSquare(posX, posY, 0.5);
        StdDraw.setPenColor(boxColor);
        StdDraw.setPenRadius(lineThickness);
        StdDraw.square(posX, posY, 0.5);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(numberColor);
        StdDraw.setFont(numberFont);
        StdDraw.text(posX, posY, String.valueOf(number));
    }

    public int getNumber() {
        // Returns the number on the tile
        return number;
    }
}




