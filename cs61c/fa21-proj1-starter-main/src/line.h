#include <stdio.h>

#ifndef LINE_H
#define LINE_H

typedef struct Line {
    void *value;
    unsigned capacity;
    unsigned length;
} Line;

Line *createLine();

void appendBytes(Line *destination, const void *src, size_t n);

void freeLine(Line *line);

char *str(Line *line);

void readLine(Line *destination, FILE *file);

void printLine(Line *line, FILE *file);

#endif  // LINE_H