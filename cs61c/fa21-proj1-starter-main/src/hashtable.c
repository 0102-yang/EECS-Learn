#include "hashtable.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

HashTable *createHashTable(int size, unsigned int (*hashFunction)(void *),
                           int (*equalFunction)(void *, void *)) {
    HashTable *newTable = malloc(sizeof(HashTable));
    if (NULL == newTable) {
        fprintf(stderr, "malloc failed \n");
        exit(1);
    }
    newTable->size = size;
    newTable->buckets = (struct HashBucketEntry **) malloc(sizeof(struct HashBucketEntry *) * size);
    if (NULL == newTable->buckets) {
        fprintf(stderr, "malloc failed \n");
        exit(1);
    }
    for (int i = 0; i < size; i++) {
        newTable->buckets[i] = NULL;
    }
    newTable->hashFunction = hashFunction;
    newTable->equalFunction = equalFunction;
    return newTable;
}

/** Task 1.2 */
void insertData(HashTable *table, void *key, void *data) {
    // DONE
    // HINT:
    // 1. Find the right hash bucket location with table->hashFunction.
    // 2. Allocate a new hash bucket entry struct.
    // 3. Append to the linked list or create it if it does not yet exist.
    HashBucketEntry *newEntry = malloc(sizeof(HashBucketEntry));
    newEntry->key = key;
    newEntry->data = data;

    unsigned index = table->hashFunction(key) % table->size;
    if (table->buckets[index] == NULL) {
        newEntry->next = NULL;
        table->buckets[index] = newEntry;
    } else {
        newEntry->next = table->buckets[index];
        table->buckets[index] = newEntry;
    }
}

/** Task 1.3 */
void *findData(HashTable *table, void *key) {
    // DONE
    // HINT:
    // 1. Find the right hash bucket with table->hashFunction.
    // 2. Walk the linked list and check for equality with table->equalFunction.
    unsigned index = table->hashFunction(key) % table->size;
    HashBucketEntry *bucket = table->buckets[index];
    void *value = NULL;

    while (bucket != NULL) {
        if (table->equalFunction(bucket->key, key) != 0) {
            value = bucket->data;
            break;
        }
        bucket = bucket->next;
    }

    return value;
}

static void freeHashTableEntry(HashBucketEntry *entry) {
    if (!entry) {
        return;
    }
    freeHashTableEntry(entry->next);
    free(entry->data);
    free(entry->key);
    free(entry);
}

void freeHashTable(HashTable *table) {
    for (unsigned i = 0; i < table->size; i++) {
        freeHashTableEntry(table->buckets[i]);
    }
    free(table->buckets);
    free(table);
}

/** Task 2.1 */
unsigned int stringHash(void *s) {
    // DONE
    // fprintf(stderr, "need to implement stringHash\n");
    /* To suppress compiler warning until you implement this function, */
    unsigned hash = 0;
    while (s && *(char *) s != '\0') {
        hash = 31 * hash + *(char *) (s++);
    }
    return hash;
}

/** Task 2.2 */
int stringEquals(void *s1, void *s2) {
    // DONE
    // fprintf(stderr, "You need to implement stringEquals");
    /* To suppress compiler warning until you implement this function */
    if (!s1 || !s2) {
        return 0;
    }
    unsigned length1 = strlen(s1);
    unsigned length2 = strlen(s2);
    if (length1 != length2) {
        return 0;
    }

    for (unsigned i = 0; i < length1; i++) {
        char c1 = *(char *) (s1++);
        char c2 = *(char *) (s2++);
        if (c1 != c2) {
            return 0;
        }
    }

    return 1;
}