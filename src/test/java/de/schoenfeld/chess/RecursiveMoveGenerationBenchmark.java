package de.schoenfeld.chess;

import de.schoenfeld.chess.board.BoardUtility;
import de.schoenfeld.chess.model.GameState;
import de.schoenfeld.chess.model.StandardPieceType;
import de.schoenfeld.chess.move.Move;
import de.schoenfeld.chess.rules.MoveGenerator;
import de.schoenfeld.chess.rules.Rules;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)  // Measure average execution time per method call
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Results in milliseconds
@State(Scope.Thread)  // Each thread gets its own instance
@Warmup(iterations = 10, time = 500, timeUnit = TimeUnit.MILLISECONDS)  // Pre-warm JVM
@Measurement(iterations = 10, time = 500, timeUnit = TimeUnit.MILLISECONDS)  // Actual measurement
@Fork(1)  // Run in the same JVM
public class RecursiveMoveGenerationBenchmark {

    private MoveGenerator<StandardPieceType> moveGenerator;
    private GameState<StandardPieceType> initialGameState;

    @Setup(Level.Trial)  // Setup only once per benchmark trial
    public void setup() {
        moveGenerator = Rules.standard();
        initialGameState = new GameState<>(BoardUtility.getDefaultBoard());
    }

    @Benchmark
    public void recursiveMoveGeneration(Blackhole blackhole) {
        long nodes = generateMovesRecursively(initialGameState, 3);
        blackhole.consume(nodes);
    }

    private long generateMovesRecursively(GameState<StandardPieceType> state, int depth) {
        if (depth == 0) {
            return 1; // Leaf node
        }

        List<Move<StandardPieceType>> moves = moveGenerator.generateMoves(state);
        long totalNodes = 0;

        for (Move<StandardPieceType> move : moves) {
            move.executeOn(state);
            totalNodes += generateMovesRecursively(state, depth - 1);
            move.undoOn(state);
        }

        return totalNodes;
    }
}
