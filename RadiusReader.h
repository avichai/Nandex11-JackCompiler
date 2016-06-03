/**
 * The header file for the Separator function.
 * Define the maximal line length and the maximal dimenssion
 * Define the Tag enum and the tagNewDataPoint function
 */
#ifndef RADIUS_READER_H
#define RADIUS_READER_H

#include <stdio.h>

#define MAX_DEPTH 1000
#define MAX_LINE_SIZE 60
#define FAIL_CODE -1
#define SUCC_CODE 0

/**
 * Read a vector of unsigned ints from a given FILE
 * each line contains a single unsigned integer
 * @return number of lines read
 */
unsigned int readVector(FILE * file, unsigned int vec[MAX_DEPTH]);

/**
 * Read a single unsigned int from a given FILE
 * each line contains a single unsigned integer
 * @return SUCC_CODE iff successful, FAIL_CODE otherwise
 */
int readSingleUInt(FILE * file, unsigned int * val);

#endif

