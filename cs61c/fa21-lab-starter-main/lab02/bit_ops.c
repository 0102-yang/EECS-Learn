#include "bit_ops.h"

#include <stdio.h>

/* Returns the Nth bit of X. Assumes 0 <= N <= 31. */
unsigned get_bit(unsigned x, unsigned n) {
    /* YOUR CODE HERE */
    return (x >> n) & 1U; /* UPDATE WITH THE CORRECT RETURN VALUE*/
}

/* Set the nth bit of the value of x to v. Assumes 0 <= N <= 31, and V is 0 or 1
 */
void set_bit(unsigned *x, unsigned n, unsigned v) { /* YOUR CODE HERE */
    *x = v == 1 ? (*x) | (1 << n) : (*x) & ~(1 << n);
}

/* Flips the Nth bit in X. Assumes 0 <= N <= 31.*/
void flip_bit(unsigned *x, unsigned n) { /* YOUR CODE HERE */
    unsigned target_bit = get_bit(*x, n);
    set_bit(x, n, target_bit == 1U ? 0U : 1U);
}
