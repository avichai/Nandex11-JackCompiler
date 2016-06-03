/**
 * This is the header file for the drawing part of the program.
 * Define number of rows and columns and prototype of two functions.
 */
#ifndef DRAW_2D_CHAR_ARRAY_H
#define DRAW_2D_CHAR_ARRAY_H

#define ROWS 100
#define COLS 71

/**
 * Initialize the given char array to be filled with space (' ') characters.
 */
void initializeArray(char arr[ROWS][COLS]);


/**
 * Draw the char array on the screen.
 * After each row go down a line ('\n').
 * Stop drawing when a nul character ('\0') is met.
 */
void drawArray(char arr[ROWS][COLS]);

#endif
