import java.awt.event.KeyEvent;
import java.util.*;

public class EightPuzzle {
    public static void main(String[] args) {
        StdDraw.setCanvasSize(500, 500);
        StdDraw.setScale(0.5, 3.5);
        StdDraw.enableDoubleBuffering();

        Board board = new Board();
        board.draw();
        StdDraw.show();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("1. Solve manually");
        System.out.println("2. Solve automatically using A* algorithm");
        int choice = scanner.nextInt();

        if (choice == 1) {
            solveManually(board);
        } else if (choice == 2) {
            solveAutomatically(board);
        } else {
            System.out.println("Invalid choice. Exiting program.");
        }
    }

    private static void solveManually(Board board) {
        // Allows the user to solve the puzzle manually using arrow keys
        while (!isGoal(board)) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                board.moveRight();
                board.draw();
                StdDraw.show();
                StdDraw.pause(200);
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                board.moveLeft();
                board.draw();
                StdDraw.show();
                StdDraw.pause(200);
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                board.moveUp();
                board.draw();
                StdDraw.show();
                StdDraw.pause(200);
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                board.moveDown();
                board.draw();
                StdDraw.show();
                StdDraw.pause(200);
            }
        }
        System.out.println("Puzzle solved!");
    }

    private static void solveAutomatically(Board board) {
        // Solves the puzzle automatically using the A* algorithm
        List<String> solution = solve(board);
        if (solution == null) {
            System.out.println("No solution found");
            return;
        }
        System.out.println("Solution: " + String.join(", ", solution));
        for (String move : solution) {
            switch (move) {
                case "R":
                    board.moveRight();
                    break;
                case "L":
                    board.moveLeft();
                    break;
                case "U":
                    board.moveUp();
                    break;
                case "D":
                    board.moveDown();
                    break;
            }
            board.draw();
            StdDraw.show();
            StdDraw.pause(500);
        }
        System.out.println("Puzzle solved!");
    }

    public static List<String> solve(Board initialBoard) {
        // Implements the A* algorithm to find the shortest path to solve the puzzle
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<Board, Integer> gScore = new HashMap<>();
        Map<Board, String> cameFrom = new HashMap<>();

        openSet.add(new Node(initialBoard, null, 0, heuristic(initialBoard)));
        gScore.put(initialBoard, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (isGoal(current.board)) {
                return reconstructPath(cameFrom, current.board);
            }

            for (String move : Arrays.asList("R", "L", "U", "D")) {
                Board neighbor = makeMove(current.board, move);
                if (neighbor == null) {
                    continue;
                }

                int tentativeGScore = gScore.get(current.board) + 1;
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, move);
                    gScore.put(neighbor, tentativeGScore);
                    openSet.add(new Node(neighbor, move, tentativeGScore, heuristic(neighbor)));
                }
            }
        }

        return null; // Should never happen for solvable 8-puzzle instances
    }

    private static Board makeMove(Board board, String move) {
        // Applies a move to the board and returns the resulting board
        Board newBoard = board.clone();
        boolean success = false;
        switch (move) {
            case "R":
                success = newBoard.moveRight();
                break;
            case "L":
                success = newBoard.moveLeft();
                break;
            case "U":
                success = newBoard.moveUp();
                break;
            case "D":
                success = newBoard.moveDown();
                break;
        }
        return success ? newBoard : null;
    }

    private static boolean isGoal(Board board) {
        // Checks if the board is in the goal state
        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        return Arrays.deepEquals(board.getTilesAsArray(), goal);
    }

    private static int heuristic(Board board) {
        // Calculates the Manhattan distance heuristic for the board
        int[][] tiles = board.getTilesAsArray();
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j] == 0) continue;
                int goalRow = (tiles[i][j] - 1) / 3;
                int goalCol = (tiles[i][j] - 1) % 3;
                sum += Math.abs(i - goalRow) + Math.abs(j - goalCol);
            }
        }
        return sum;
    }

    private static List<String> reconstructPath(Map<Board, String> cameFrom, Board current) {
        // Reconstructs the path to the solution from the cameFrom map
        List<String> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            String move = cameFrom.get(current);
            path.add(move);
            current = makeMove(current, oppositeMove(move));
        }
        Collections.reverse(path);
        return path;
    }

    private static String oppositeMove(String move) {
        // Returns the opposite of a given move
        switch (move) {
            case "R":
                return "L";
            case "L":
                return "R";
            case "U":
                return "D";
            case "D":
                return "U";
            default:
                throw new IllegalArgumentException("Invalid move: " + move);
        }
    }

    private static class Node {
        // Represents a node in the A* algorithm, containing a board state and its associated costs
        Board board;
        String move;
        int g, h;

        Node(Board board, String move, int g, int h) {
            this.board = board;
            this.move = move;
            this.g = g; // Cost from the start node to this node
            this.h = h; // Heuristic cost from this node to the goal
        }

        int getF() {
            return g + h; // Total cost of this node
        }
    }
}
