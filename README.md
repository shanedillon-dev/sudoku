# Sudoku solver

A Kotlin sudoku solver (TU Dublin Lab 10).

## Running a release

1. Download `app.zip` from the Releases page and unzip it.
2. Run `bin/sudoku <board-file>` (or `bin\sudoku.bat <board-file>` on Windows). Requires Java 17+.

## Board file format

9 lines, 9 cells per line. Digits 1-9 are given numbers; `0` or `.` is an empty cell.
Spaces and `|` are ignored. Examples are in `boards/`.

```
530070000
600195000
098000060
800060003
400803001
700020006
060000280
000419005
000080079
```

If the board can't be solved within 2 million iterations, the program prints
"Could not find a solution".

## Building from source

```
./gradlew build     # compile + run tests
./gradlew run --args="../boards/easy.txt"
```
