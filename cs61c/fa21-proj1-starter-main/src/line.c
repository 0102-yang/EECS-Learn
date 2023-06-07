#include "line.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

static void extendLine(Line *line) {
    unsigned newCapacity = line->capacity << 1;
    void *tmp = line->value;

    line->capacity = newCapacity;
    line->value = malloc(newCapacity);
    memset(line->value, 0, newCapacity);
    memcpy(line->value, tmp, line->length);

    free(tmp);
}

Line *createLine() {
    unsigned defaultCapacity = 4;

    Line *line = malloc(sizeof(Line));
    line->capacity = defaultCapacity;
    line->length = 0;
    line->value = malloc(defaultCapacity);
    memset(line->value, 0, defaultCapacity);

    return line;
}

void appendBytes(Line *destination, const void *src, size_t n) {
    unsigned newLength = destination->length + n;
    while (destination->capacity < newLength) {
        extendLine(destination);
    }

    void *copyStart = destination->value + destination->length;
    memcpy(copyStart, src, n);
    destination->length = newLength;
}

void freeLine(Line *line) {
    free(line->value);
    free(line);
}

char *str(Line *line) { return (char *) line->value; }

void readLine(Line *destination, FILE *file) {
    char c;
    while ((c = (char) fgetc(file)) != EOF) {
        appendBytes(destination, &c, 1);
        if (c == '\n') {
            break;
        }
    }
}

void printLine(Line *line, FILE *file) {
    unsigned length = line->length;
    char *start = line->value;
    for (unsigned i = 0; i < length; i++) {
        fputc(*(start++), file);
    }
}