package ie.tudublin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class SudokuTest {

    private fun parse(s: String): IntArray =
        s.filter { !it.isWhitespace() }.map { if (it == '.') 0 else it - '0' }.toIntArray()

    @Test fun solvesEasyBoard() {
        val board = parse(
            "53..7....6..195....98....6.8...6...34..8.3..17...2...6.6....28....419..5....8..79"
        )
        val expected = parse(
            "534678912672195348198342567859761423426853791713924856961537284287419635345286179"
        )
        val sudoku = Sudoku(board)
        assertTrue(sudoku.solve())
        assertArrayEquals(expected, sudoku.solution)
    }

    @Test fun solvesHardBoard() {
        // Arto Inkala's "world's hardest sudoku"
        val board = parse(
            "8..........36......7..9.2...5...7.......457.....1...3...1....68..85...1..9....4.."
        )
        val expected = parse(
            "812753649943682175675491283154237896369845721287169534521974368438526917796318452"
        )
        val sudoku = Sudoku(board)
        assertTrue(sudoku.solve())
        assertArrayEquals(expected, sudoku.solution)
    }

    @Test fun givesUpOnUnsolvableBoard() {
        // No duplicate givens, but no solution exists. A plain backtracking
        // solver would take ages on this, so it should hit the 2 million
        // iteration limit and report failure.
        val board = parse(
            ".....5.8....6.1.43..........1.5........1.6...3.......553.....61........4........."
        )
        val sudoku = Sudoku(board)
        assertFalse(sudoku.solve())
        assertNull(sudoku.solution)
        assertTrue(sudoku.iterations > Sudoku.DEFAULT_MAX_ITERATIONS)
    }
}
