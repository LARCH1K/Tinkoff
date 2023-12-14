package edu.hw9.task3;

import edu.project2.components.Cell;
import edu.project2.components.Coordinate;
import edu.project2.components.Maze;
import edu.project2.solvers.Solver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDFSSolver implements Solver {
    private final int numThreads;

    public ParallelDFSSolver(int numThreads) {
        this.numThreads = numThreads;
    }

    @Override
    public List<Coordinate> solve(final Maze maze, final Coordinate start, final Coordinate end) {
        if (!isValidValues(maze, start, end)) {
            throw new IllegalArgumentException();
        }

        Coordinate indexStart = new Coordinate(start.row() - 1, start.col() - 1);
        Coordinate indexEnd = new Coordinate(end.row() - 1, end.col() - 1);
        if (maze.getGrid()[indexStart.row()][indexStart.col()].getType() == Cell.Type.WALL
            || maze.getGrid()[indexEnd.row()][indexEnd.col()].getType() == Cell.Type.WALL) {
            return new ArrayList<>();
        }

        Coordinate[][] pathArr = new Coordinate[maze.getHeight()][maze.getWidth()];
        boolean[][] visited = new boolean[maze.getHeight()][maze.getWidth()];
        Deque<Coordinate> deque = new ConcurrentLinkedDeque<>();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        visited[indexStart.row()][indexStart.col()] = true;
        deque.addLast(indexStart);

        while (!deque.isEmpty()) {
            List<Future<Void>> futures = new ArrayList<>();
            final Coordinate curr = deque.pop();
            futures.add(executorService.submit(() -> {
                List<Coordinate> neighbors = getNeighbors(maze, curr, visited);
                for (Coordinate neighbor : neighbors) {
                    pathArr[neighbor.row()][neighbor.col()] = curr;
                    visited[neighbor.row()][neighbor.col()] = true;
                    deque.addLast(neighbor);
                }
                return null;
            }));
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        executorService.shutdown();

        Coordinate pathCord = indexEnd;
        List<Coordinate> path = new ArrayList<>();
        while (pathCord != null) {
            path.add(pathCord);
            pathCord = pathArr[pathCord.row()][pathCord.col()];
        }
        executorService.close();
        Collections.reverse(path);
        return path;
    }

    private boolean isValidValues(final Maze maze, final Coordinate start, final Coordinate end) {
        if (maze == null || start == null || end == null) {
            return false;
        }
        if (start.col() < 1 || start.row() < 1 || end.row() < 1 || end.col() < 1
            || start.col() > maze.getWidth() || start.row() > maze.getHeight()
            || end.row() > maze.getHeight() || end.col() > maze.getWidth()) {
            return false;
        }
        return true;
    }

    private List<Coordinate> getNeighbors(final Maze maze, final Coordinate coordinate, final boolean[][] visited) {
        List<Coordinate> neighbors = new ArrayList<>();
        int[][] moves = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] move : moves) {
            int newRow = coordinate.row() + move[0];
            int newCol = coordinate.col() + move[1];

            if (newRow >= 0 && newRow < maze.getHeight() && newCol >= 0 && newCol < maze.getWidth()
                && maze.getGrid()[newRow][newCol].getType() == Cell.Type.PASSAGE && !visited[newRow][newCol]) {
                neighbors.add(new Coordinate(newRow, newCol));
            }
        }
        return neighbors;
    }
}
