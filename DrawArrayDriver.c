/**
 * This is a driver example for the Draw2DCharArray.c implementation file
 */
#include <stdio.h>
#include "Draw2DCharArray.h"

char arr[ROWS][COLS];

/**
 * Fill the global array arr with a smiley.
 * Should print a figure like:
 *                                 () ()                                
 *                                   |                                  
 *                                   |                                  
 *                                   |                                  
 *                                 \   /                                
 *                                  \_/                                 
 */
void fillArray1()
{
	int middleCol = COLS / 2 + 1;
	int middleRow = 20;

	arr[middleRow+1][0] = '\0';

	arr[middleRow][middleCol] = '_';

	arr[middleRow][middleCol + 1] = '/';
	arr[middleRow-1][middleCol + 2] = '/';

	arr[middleRow][middleCol-1] = '\\';
	arr[middleRow-1][middleCol-2] = '\\';

	arr[middleRow - 2][middleCol] = '|';
	arr[middleRow - 3][middleCol] = '|';
	arr[middleRow - 4][middleCol] = '|';

	arr[middleRow - 5][middleCol + 1] = '(';
	arr[middleRow - 5][middleCol + 2] = ')';

	arr[middleRow - 5][middleCol - 1] = ')';
	arr[middleRow - 5][middleCol - 2] = '(';
}

/**
 * Some characters are not visible on the screen, so make sure the char is
 * from the "visible" part of the ASCII table:
 */
char makeSureVisibleChar(char c)
{
	if ((c < ' ') || (c > '~'))
	{
		return ' ';
	}
	else
	{
		return c;
	}
}

/**
 * Fill the global 2D array with different characters,
 * starting with the char startWith
 */
void fillArray2(char startWith)
{
	int row, col;

	arr[0][0] = makeSureVisibleChar(startWith);

	for (col = 1; col < COLS; col ++)
	{
		arr[0][col] = makeSureVisibleChar(arr[0][col - 1] + 1);
	}

	for (row = 1; row < ROWS; row ++)
	{
		arr[row][0] = makeSureVisibleChar(arr[row - 1][0] + 1);
		for (col = 1; col < COLS; col ++)
		{
			arr[row][col] = makeSureVisibleChar(arr[row][col - 1] + 1);
		}
	}
}

/**
 * The main function that ask the user for the number of test he wants (1-3)
 */
int main()
{

	printf("Select 1, 2 or 3:\n");
	int choice;
	scanf("%d", &choice);

	initializeArray(arr);

	switch (choice)
	{
	  case 1:
		  fillArray1();
		  break;
	  case 2:
		  fillArray2(' ');
		  break;
	  case 3:
		  fillArray2('B');
		  break;
	  default: printf("bad choice.\n");
		  return -1;
	}

	drawArray(arr);

	return 0;
}
