package de.schoenfeld.chess;

import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.rules.MoveGenerator;
import de.schoenfeld.chess.rules.Rules;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)  // Measure average execution time per method call
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Results in milliseconds
@State(Scope.Thread)  // Each thread gets its own instance
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)  // Pre-warm JVM
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)  // Actual measurement
@Fork(1)  // Run in the same JVM
public class RecursiveMoveGenerationBenchmark {

    private MoveGenerator moveGenerator;
    private GameState initialGameState;

    @Setup(Level.Trial)  // Setup only once per benchmark trial
    public void setup() {
        moveGenerator = Rules.DEFAULT;
        initialGameState = new GameState(BoardUtility.getDefaultBoard());
    }

    @Benchmark
    public long recursiveMoveGeneration() {
        return generateMovesRecursively(initialGameState, 3);
    }

    private long generateMovesRecursively(GameState state, int depth) {
        if (depth == 0) {
            return 1; // Leaf node
        }

        List<Move> moves = moveGenerator.generateMoves(state);
        long totalNodes = 0;

        for (Move move : moves) {
            GameState newState = move.executeOn(state);
            totalNodes += generateMovesRecursively(newState, depth - 1);
        }

        return totalNodes;
    }
}
