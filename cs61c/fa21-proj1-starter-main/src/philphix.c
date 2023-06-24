/*
 * Include the provided hash table library.
 */
#include "hashtable.h"

/*
 * Include the header file.
 */
#include "philphix.h"

/*
 * Standard IO and file routines.
 */
#include <stdio.h>

/*
 * General utility routines (including malloc()).
 */
#include <stdlib.h>

/*
 * Character utility routines.
 */
#include <ctype.h>

/*
 * String utility routines.
 */
#include <string.h>
#include "line.h"

/*
 * This hash table stores the dictionary.
 */
HashTable *dictionary;

/*
 * The MAIN routine.  You can safely print debugging information
 * to standard error (stderr) as shown and it will be ignored in
 * the grading process.
 */
#ifndef _PHILPHIX_UNITTEST

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "Specify a dictionary\n");
        return 1;
    }

    /*
     * Allocate a hash table to store the dictionary.
     */
    fprintf(stderr, "Creating hashtable\n");
    dictionary = createHashTable(0x61C, &stringHash, &stringEquals);

    fprintf(stderr, "Loading dictionary %s\n", argv[1]);
    readDictionary(argv[1]);
    fprintf(stderr, "Dictionary loaded\n");

    fprintf(stderr, "Processing stdin\n");
    processInput();

    freeHashTable(dictionary);

    /*
     * The MAIN function in C should always return 0 as a way of telling
     * whatever program invoked this that everything went OK.
     */
    return 0;
}

#endif /* _PHILPHIX_UNITTEST */

void splitKeyData(char *str, char **key, char **data) {
    unsigned length = strlen(str), index = 0;
    char *tmp = str;
    char buffer[length + 1];

    // Split key.
    while (isalnum(*tmp) || ispunct(*tmp)) {
        buffer[index++] = *(tmp++);
    }
    buffer[index] = '\0';
    *key = malloc(sizeof(char) * ++index);
    strncpy(*key, buffer, index);

    // Erase redundant space or tab.
    while (*tmp == '\t' || *tmp == ' ') {
        tmp++;
    }

    // Split data.
    index = 0;
    while (isalnum(*tmp) || ispunct(*tmp)) {
        buffer[index++] = *(tmp++);
    }
    buffer[index] = '\0';
    *data = malloc(sizeof(char) * ++index);
    strncpy(*data, buffer, index);
}

/* Task 3 */
void readDictionary(char *dictName) {
    // DONE
    // fprintf(stderr, "You need to implement readDictionary\n");
    FILE *dict = fopen(dictName, "r");
    if (dict == NULL) {
        perror("Can not read file.");
        exit(61);
    }

    while (1) {
        Line *line = createLine();
        readLine(line, dict);
        if (line->length == 0) {
            freeLine(line);
            break;
        }
        char l[line->length + 1], *key, *data;
        strncpy(l, line->value, line->length);
        l[line->length] = '\0';
        splitKeyData(l, &key, &data);
        insertData(dictionary, key, data);
        freeLine(line);
    }
}

/**
 * Replace word by dictionary.
 * @param originalWord Original word.
 * @return new word.
 */
char *replaceWord(char *originalWord) {
    unsigned length = strlen(originalWord);
    char tmp[length + 1];
    strncpy(tmp, originalWord, length + 1);

    // 1. Find by the exact word.
    char *replace = (char *) findData(dictionary, tmp);
    // 2. Find by the word with every alphabetical character
    // except the first character converted to lowercase.
    if (!replace && strlen(tmp) >= 1) {
        char *t = tmp + 1;
        while (*t != '\0') {
            if (isupper(*t)) {
                *t = (char) tolower(*t);
            }
            ++t;
        }
        replace = (char *) findData(dictionary, tmp);
    }
    // 3. Find by every alphabetical character of the word
    // converted to lowercase.
    if (!replace && isupper(*tmp)) {
        *tmp = (char) tolower(*tmp);
        replace = (char *) findData(dictionary, tmp);
    }

    // Copy result.
    char *res;
    if (replace) {
        res = malloc(sizeof(char) * (strlen((replace)) + 1));
        strcpy(res, replace);
    } else {
        res = malloc(sizeof(char) * (length + 1));
        strncpy(res, originalWord, length + 1);
    }
    return res;
}

/**
 * Transfer the original line to new line by dictionary.
 * @param originalLine The original line.
 * @return New line.
 */
Line *transferLine(Line *originalLine) {
    Line *newLine = createLine();
    char *string = str(originalLine);
    unsigned index = 0, length = originalLine->length;
    char buffer[length + 1];

    while (index < length) {
        if (isalnum(*string)) {
            unsigned count = 0;
            buffer[count++] = *(string++);
            index++;

            while (index < length && isalnum(*string)) {
                buffer[count++] = *(string++);
                index++;
            }
            buffer[count] = '\0';

            char *newWord = replaceWord(buffer);
            appendBytes(newLine, newWord, strlen(newWord));
            free(newWord);
        } else {
            appendBytes(newLine, string++, 1);
            index++;
        }
    }

    return newLine;
}

/* Task 4 */
void processInput() {
    // fprintf(stderr, "You need to implement processInput\n");
    while (1) {
        Line *originalLine = createLine();
        readLine(originalLine, stdin);
        if (originalLine->length == 0) {
            freeLine(originalLine);
            break;
        }

        Line *newLine = transferLine(originalLine);
        printLine(newLine, stdout);

        freeLine(originalLine);
        freeLine(newLine);
    }
}