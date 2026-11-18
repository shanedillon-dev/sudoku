package ie.tudublin

/**
 * Backtracking sudoku solver.
 *
 * The board is a 9x9 grid stored as an IntArray of 81 cells, row by row.
 * Empty cells are 0. This class only does the solving - all input/output
 * is handled in App.kt.
 */
class Sudoku(board: IntArray, private val maxIterations: Long = DEFAULT_MAX_ITERATIONS) {

    companion object {
        const val DEFAULT_MAX_ITERATIONS = 2_000_000L
    }

    private val grid: IntArray = board.copyOf()

    /** Number of placement attempts made by the last call to solve(). */
    var iterations: Long = 0
        private set

    /** The solved board, or null if no solution was found. */
    var solution: IntArray? = null
        private set

    init {
        require(board.size == 81) { "Board must have exactly 81 cells" }
        require(board.all { it in 0..9 }) { "Cells must be between 0 and 9" }
    }

    /**
     * Tries to solve the board. Returns true if solved, false if the board
     * is invalid, has no solution, or takes more than maxIterations steps.
     */
    fun solve(): Boolean {
        iterations = 0
        solution = null
        if (!givensAreValid()) return false
        return try {
            if (backtrack(0)) {
                solution = grid.copyOf()
                true
            } else {
                false
            }
        } catch (e: GiveUpException) {
            false
        }
    }

    private class GiveUpException : RuntimeException()

    private fun backtrack(start: Int): Boolean {
        var cell = start
        while (cell < 81 && grid[cell] != 0) cell++
        if (cell == 81) return true

        val row = cell / 9
        val col = cell % 9
        for (n in 1..9) {
            if (++iterations > maxIterations) throw GiveUpException()
            if (canPlace(row, col, n)) {
                grid[cell] = n
                if (backtrack(cell + 1)) return true
                grid[cell] = 0
            }
        }
        return false
    }

    private fun canPlace(row: Int, col: Int, n: Int): Boolean {
        for (i in 0 until 9) {
            if (grid[row * 9 + i] == n) return false
            if (grid[i * 9 + col] == n) return false
        }
        val boxRow = row / 3 * 3
        val boxCol = col / 3 * 3
        for (r in boxRow until boxRow + 3) {
            for (c in boxCol until boxCol + 3) {
                if (grid[r * 9 + c] == n) return false
            }
        }
        return true
    }

    /** Checks that the starting numbers don't already break the rules. */
    private fun givensAreValid(): Boolean {
        for (cell in 0 until 81) {
            val n = grid[cell]
            if (n == 0) continue
            grid[cell] = 0
            val ok = canPlace(cell / 9, cell % 9, n)
            grid[cell] = n
            if (!ok) return false
        }
        return true
    }
}
