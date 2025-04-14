package de.schoenfeld.chesskit;

import de.schoenfeld.chesskit.board.BoardUtility;
import de.schoenfeld.chesskit.board.tile.Square8x8;
import de.schoenfeld.chesskit.model.GameState;
import de.schoenfeld.chesskit.model.StandardPieceType;
import de.schoenfeld.chesskit.move.Move;
import de.schoenfeld.chesskit.rules.MoveGenerator;
import de.schoenfeld.chesskit.rules.Rules;
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

    private MoveGenerator<Square8x8, StandardPieceType> moveGenerator;
    private GameState<Square8x8, StandardPieceType> initialGameState;

    @Setup(Level.Trial)  // Setup only once per benchmark trial
    public void setup() {
        moveGenerator = Rules.standard();
        initialGameState = new GameState<>(BoardUtility.getDefaultBoard(), Rules.standard());
    }

    @Benchmark
    public void recursiveMoveGeneration(Blackhole blackhole) {
        long nodes = generateMovesRecursively(initialGameState, 4);
        blackhole.consume(nodes);
    }

    private long generateMovesRecursively(GameState<Square8x8, StandardPieceType> state, int depth) {
        if (depth == 0) {
            return 1; // Leaf node
        }

        List<Move<Square8x8, StandardPieceType>> moves = moveGenerator.generateMoves(state);
        long totalNodes = 0;

        for (Move<Square8x8, StandardPieceType> move : moves) {
            state.makeMove(move);
            totalNodes += generateMovesRecursively(state, depth - 1);
            state.unmakeLastMove();
        }

        return totalNodes;
    }
}
