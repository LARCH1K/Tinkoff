package edu.project2;

import edu.project2.components.Cell;
import edu.project2.components.Coordinate;
import edu.project2.components.Maze;
import edu.project2.solvers.DFSSolver;
import edu.project2.solvers.Solver;
import edu.project2.solvers.WaveSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {

    Maze maze;

    @BeforeEach
    void beforeEach(){
        maze = new Maze(3,3);
        initializeMaze(maze, 3, 3);
        maze.getGrid()[0][0].setType(Cell.Type.PASSAGE);
        maze.getGrid()[0][1].setType(Cell.Type.PASSAGE);
        maze.getGrid()[1][1].setType(Cell.Type.PASSAGE);
        maze.getGrid()[2][1].setType(Cell.Type.PASSAGE);
        maze.getGrid()[2][2].setType(Cell.Type.PASSAGE);
        maze.getGrid()[0][2].setType(Cell.Type.PASSAGE);
        maze.getGrid()[2][0].setType(Cell.Type.PASSAGE);
    }

    @Test
    void solverDFSTest(){
        Solver solver = new DFSSolver();
        List<Coordinate> path = solver.solve(maze, new Coordinate(1,1),new Coordinate(3,3));
        assertThat(path).hasSize(5)
            .contains(new Coordinate(0,0))
            .contains(new Coordinate(0,1))
            .contains(new Coordinate(1,1))
            .contains(new Coordinate(2,1))
            .contains(new Coordinate(2,2));
    }

    @Test
    void waveSolverTest(){
        Solver solver = new WaveSolver();
        List<Coordinate> path = solver.solve(maze, new Coordinate(1,1),new Coordinate(3,3));
        assertThat(path).hasSize(5)
            .contains(new Coordinate(0,0))
            .contains(new Coordinate(0,1))
            .contains(new Coordinate(1,1))
            .contains(new Coordinate(2,1))
            .contains(new Coordinate(2,2));
    }

    @Test
    void solverDFSWithInputWallCellTest(){
        Solver solver = new DFSSolver();
        List<Coordinate> path = solver.solve(maze, new Coordinate(2,1),new Coordinate(3,3));
        assertThat(path).isEmpty();
        path = solver.solve(maze, new Coordinate(1,1),new Coordinate(2,3));
        assertThat(path).isEmpty();
    }

    @Test
    void WaveSolverWithInputWallCellTest(){
        Solver solver = new DFSSolver();
        List<Coordinate> path = solver.solve(maze, new Coordinate(2,1),new Coordinate(3,3));
        assertThat(path).isEmpty();
        path = solver.solve(maze, new Coordinate(2,1),new Coordinate(2,3));
        assertThat(path).isEmpty();
    }

    private void initializeMaze(final Maze maze, final int height, final int width) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze.getGrid()[i][j] = new Cell(i, j, Cell.Type.WALL);
            }
        }
    }
}
