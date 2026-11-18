package ie.tudublin

import java.io.File
import kotlin.system.exitProcess

/**
 * Reads a board from a text file: 9 lines, 9 cells per line.
 * Digits 1-9 are givens; 0, '.' or '_' mean an empty cell.
 * Spaces, commas and '|' are ignored, as are blank lines.
 */
fun readBoard(file: File): IntArray {
    val rows = file.readLines()
        .map { line -> line.filterNot { it.isWhitespace() || it == ',' || it == '|' } }
        .filter { it.isNotEmpty() && !it.all { c -> c == '-' || c == '+' } }

    require(rows.size == 9) { "Expected 9 rows but found ${rows.size}" }

    val cells = mutableListOf<Int>()
    rows.forEachIndexed { r, row ->
        require(row.length == 9) { "Row ${r + 1} should have 9 cells but has ${row.length}" }
        for (c in row) {
            cells += when (c) {
                in '1'..'9' -> c - '0'
                '0', '.', '_' -> 0
                else -> throw IllegalArgumentException("Invalid character '$c' on row ${r + 1}")
            }
        }
    }
    return cells.toIntArray()
}

fun printBoard(board: IntArray) {
    for (r in 0 until 9) {
        if (r % 3 == 0 && r != 0) println("------+-------+------")
        val line = StringBuilder()
        for (c in 0 until 9) {
            if (c % 3 == 0 && c != 0) line.append("| ")
            val n = board[r * 9 + c]
            line.append(if (n == 0) "." else n.toString()).append(' ')
        }
        println(line.toString().trimEnd())
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: sudoku <board-file>")
        exitProcess(1)
    }

    val file = File(args[0])
    if (!file.exists()) {
        System.err.println("File not found: ${args[0]}")
        exitProcess(1)
    }

    val board = try {
        readBoard(file)
    } catch (e: IllegalArgumentException) {
        System.err.println("Could not read board: ${e.message}")
        exitProcess(1)
    }

    println("Input board:")
    printBoard(board)
    println()

    val sudoku = Sudoku(board)
    if (sudoku.solve()) {
        println("Solution (${sudoku.iterations} iterations):")
        printBoard(sudoku.solution!!)
    } else {
        println("Could not find a solution (gave up after ${sudoku.iterations} iterations).")
        exitProcess(2)
    }
}
