#include "simd.h"

#include <stdio.h>
#include <time.h>
#include <x86intrin.h>

long long int sum(int vals[NUM_ELEMS]) {
    clock_t start = clock();

    long long int sum = 0;
    for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
        for (unsigned int i = 0; i < NUM_ELEMS; i++) {
            if (vals[i] >= 128) {
                sum += vals[i];
            }
        }
    }
    clock_t end = clock();
    printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
    return sum;
}

long long int sum_unrolled(int vals[NUM_ELEMS]) {
    clock_t start = clock();
    long long int sum = 0;

    for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
        for (unsigned int i = 0; i < NUM_ELEMS / 4 * 4; i += 4) {
            if (vals[i] >= 128) sum += vals[i];
            if (vals[i + 1] >= 128) sum += vals[i + 1];
            if (vals[i + 2] >= 128) sum += vals[i + 2];
            if (vals[i + 3] >= 128) sum += vals[i + 3];
        }

        // TAIL CASE, for when NUM_ELEMS isn't a multiple of 4
        // NUM_ELEMS / 4 * 4 is the largest multiple of 4 less than NUM_ELEMS
        // Order is important, since (NUM_ELEMS / 4) effectively rounds down
        // first
        for (unsigned int i = NUM_ELEMS / 4 * 4; i < NUM_ELEMS; i++) {
            if (vals[i] >= 128) {
                sum += vals[i];
            }
        }
    }
    clock_t end = clock();
    printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
    return sum;
}

long long int sum_simd(int vals[NUM_ELEMS]) {
    clock_t start = clock();
    __m128i _127 = _mm_set1_epi32(
        127);  // This is a vector with 127s in it... Why might you need this?
    long long int result =
        0;  // This is where you should put your final result!
    /* DO NOT MODIFY ANYTHING ABOVE THIS LINE (in this function) */

    __m128i res, new_vec, mask;
    int* vptr;
    int tmp[4], stepover = 4;
    unsigned tmp_end = NUM_ELEMS / stepover * stepover;
    for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
        res = _mm_setzero_si128();
        for (unsigned i = 0; i < tmp_end; i += stepover) {
            vptr = vals + i;
            new_vec = _mm_loadu_si128((__m128i*)vptr);
            mask = _mm_cmpgt_epi32(new_vec, _127);
            new_vec = _mm_and_si128(new_vec, mask);
            res = _mm_add_epi32(res, new_vec);
        }
        _mm_storeu_si128((__m128i*)tmp, res);
        result += (tmp[0] + tmp[1] + tmp[2] + tmp[3]);
        /* Hint: you'll need a tail case. */
        for (unsigned i = tmp_end; i < NUM_ELEMS; i++) {
            result += vals[i];
        }
    }

    /* DO NOT MODIFY ANYTHING BELOW THIS LINE (in this function) */
    clock_t end = clock();
    printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
    return result;
}

long long int sum_simd_unrolled(int vals[NUM_ELEMS]) {
    clock_t start = clock();
    __m128i _127 = _mm_set1_epi32(127);
    long long int result = 0;
    /* DO NOT MODIFY ANYTHING ABOVE THIS LINE (in this function) */

    __m128i res, new_vec, mask;
    int* vptr;
    int tmp[4], stepover = 12;
    unsigned tmp_end = NUM_ELEMS / stepover * stepover;
    for (unsigned int w = 0; w < OUTER_ITERATIONS; w++) {
        /* YOUR CODE GOES HERE */
        /* Copy your sum_simd() implementation here, and unroll it */
        res = _mm_setzero_si128();
        for (unsigned i = 0; i < tmp_end; i += stepover) {
            vptr = vals + i;
            new_vec = _mm_loadu_si128((__m128i*)vptr);
            mask = _mm_cmpgt_epi32(new_vec, _127);
            new_vec = _mm_and_si128(new_vec, mask);
            res = _mm_add_epi32(res, new_vec);

            new_vec = _mm_loadu_si128((__m128i*)(vptr + 4));
            mask = _mm_cmpgt_epi32(new_vec, _127);
            new_vec = _mm_and_si128(new_vec, mask);
            res = _mm_add_epi32(res, new_vec);

            new_vec = _mm_loadu_si128((__m128i*)(vptr + 8));
            mask = _mm_cmpgt_epi32(new_vec, _127);
            new_vec = _mm_and_si128(new_vec, mask);
            res = _mm_add_epi32(res, new_vec);
        }
        _mm_storeu_si128((__m128i*)tmp, res);
        result += (tmp[0] + tmp[1] + tmp[2] + tmp[3]);
        /* Hint: you'll need a tail case. */
        for (unsigned i = tmp_end; i < NUM_ELEMS; i++) {
            result += vals[i];
        }
        /* Hint: you'll need 1 or maybe 2 tail cases here. */
    }

    /* DO NOT MODIFY ANYTHING BELOW THIS LINE (in this function) */
    clock_t end = clock();
    printf("Time taken: %Lf s\n", (long double)(end - start) / CLOCKS_PER_SEC);
    return result;
}
