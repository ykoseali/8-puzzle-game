import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private static final Color backgroundColor = new Color(145, 234, 255);
    private static final Color boxColor = new Color(31, 160, 239);
    private static final double lineThickness = 0.02;

    private Tile[][] tiles = new Tile[3][3];
    private int emptyCellRow, emptyCellCol;

    public Board() {
        int[] numbers = new int[9];
        for (int i = 0; i < 9; i++)
            numbers[i] = i;
        randomShuffling(numbers);

        int arrayIndex = 0;
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                if (numbers[arrayIndex] != 0)
                    tiles[row][col] = new Tile(numbers[arrayIndex]);
                else {
                    emptyCellRow = row;
                    emptyCellCol = col;
                }
                arrayIndex++;
            }
    }

    private void randomShuffling(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int randIndex = (int) (Math.random() * array.length);
            if (i != randIndex) {
                int temp = array[i];
                array[i] = array[randIndex];
                array[randIndex] = temp;
            }
        }
    }

    public void draw() {
        StdDraw.clear(backgroundColor);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                if (tiles[row][col] == null)
                    continue;
                Point tilePosition = getTilePosition(row, col);
                tiles[row][col].draw(tilePosition.x, tilePosition.y);
            }
        StdDraw.setPenColor(boxColor);
        StdDraw.setPenRadius(lineThickness);
        StdDraw.square(2, 2, 1.5);
        StdDraw.setPenRadius();
    }

    private Point getTilePosition(int rowIndex, int columnIndex) {
        int posX = columnIndex + 1, posY = 3 - rowIndex;
        return new Point(posX, posY);
    }

    public int[][] getTilesAsArray() {
        // Converts the board's tiles into a 2D array of integers
        int[][] array = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array[i][j] = (tiles[i][j] != null) ? tiles[i][j].getNumber() : 0;
            }
        }
        return array;
    }

    public Board clone() {
        // Creates a deep copy of the board
        Board newBoard = new Board();
        newBoard.emptyCellRow = this.emptyCellRow;
        newBoard.emptyCellCol = this.emptyCellCol;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.tiles[i][j] != null) {
                    newBoard.tiles[i][j] = new Tile(this.tiles[i][j].getNumber());
                }
            }
        }
        return newBoard;
    }
    
    // A method for moving the empty cell right
    public boolean moveRight() {
        // the empty cell cannot go right if it is already at the rightmost column
        if (emptyCellCol == 2)
            return false; // return false as the empty cell cannot be moved
        // replace the empty cell with the tile on its right
        tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow][emptyCellCol + 1];
        tiles[emptyCellRow][emptyCellCol + 1] = null;
        // update the column index of the empty cell
        emptyCellCol++;
        // return true as the empty cell is moved successfully
        return true;
    }

    // A method for moving the empty cell left
    public boolean moveLeft() {
        // the empty cell cannot go left if it is already at the leftmost column
        if (emptyCellCol == 0)
            return false; // return false as the empty cell cannot be moved
        // replace the empty cell with the tile on its left
        tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow][emptyCellCol - 1];
        tiles[emptyCellRow][emptyCellCol - 1] = null;
        // update the column index of the empty cell
        emptyCellCol--;
        // return true as the empty cell is moved successfully
        return true;
    }

    // A method for moving the empty cell up
    public boolean moveUp() {
        // the empty cell cannot go up if it is already at the topmost row
        if (emptyCellRow == 0)
            return false; // return false as the empty cell cannot be moved
        // replace the empty cell with the tile above it
        tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow - 1][emptyCellCol];
        tiles[emptyCellRow - 1][emptyCellCol] = null;
        // update the row index of the empty cell
        emptyCellRow--;
        // return true as the empty cell is moved successfully
        return true;
    }

    // A method for moving the empty cell down
    public boolean moveDown() {
        // the empty cell cannot go down if it is already at the bottommost row
        if (emptyCellRow == 2)
            return false; // return false as the empty cell cannot be moved
        // replace the empty cell with the tile below it
        tiles[emptyCellRow][emptyCellCol] = tiles[emptyCellRow + 1][emptyCellCol];
        tiles[emptyCellRow + 1][emptyCellCol] = null;
        // update the row index of the empty cell
        emptyCellRow++;
        // return true as the empty cell is moved successfully
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        // Checks if two boards are equal based on their tile arrangements
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Board board = (Board) obj;
        return Arrays.deepEquals(this.getTilesAsArray(), board.getTilesAsArray());
    }

    @Override
    public int hashCode() {
        // Generates a hash code based on the board's tile arrangement
        return Arrays.deepHashCode(this.getTilesAsArray());
    }
}
