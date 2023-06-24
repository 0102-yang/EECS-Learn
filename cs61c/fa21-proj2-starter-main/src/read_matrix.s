.globl read_matrix

.text
# ==============================================================================
# FUNCTION: Allocates memory and reads in a binary file as a matrix of integers
#
# FILE FORMAT:
#   The first 8 bytes are two 4 byte ints representing the # of rows and columns
#   in the matrix. Every 4 bytes afterwards is an element of the matrix in
#   row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is a pointer to an integer, we will set it to the number of rows
#   a2 (int*)  is a pointer to an integer, we will set it to the number of columns
# Returns:
#   a0 (int*)  is the pointer to the matrix in memory
# Exceptions:
# - If malloc returns an error,
#   this function terminates the program with error code 88
# - If you receive an fopen error or eof,
#   this function terminates the program with error code 89
# - If you receive an fclose error or eof,
#   this function terminates the program with error code 90
# - If you receive an fread error or eof,
#   this function terminates the program with error code 91
# ==============================================================================
read_matrix:

    # Prologue
    addi sp sp -28
    sw ra 0(sp)
    sw s0 4(sp) # File descriptor.
    sw s1 8(sp) # Row integer pointer.
    sw s2 12(sp) # Col integer pointer.
    sw s3 16(sp) # Buffer for storing matrix.
    sw s4 20(sp) # Buffer to read row and col.
    sw s5 24(sp) # Bytes to store whole matrix.

    # Store arguments.
    add s1 a1 zero
    add s2 a2 zero

    # Open file.
    add a1 a0 zero
    li a2 0
    jal fopen
    addi t0 zero -1
    beq a0 t0 fopen_error
    add s0 a0 zero

    ## Read row and col.
    # Malloc 8 bytes to store row and col.
    addi a0 zero 8
    jal malloc
    beq a0 zero malloc_error
    add s4 a0 zero
    # Read row and col from file.
    add a1 s0 zero
    add a2 s4 zero
    addi a3 zero 8
    jal fread
    li t0 8
    bne a0 t0 fread_error
    # Load row and col.
    lw t0 0(s4)
    sw t0 0(s1)
    lw t0 4(s4)
    sw t0 0(s2)

    ## Read matrix.
    # Calculate amount of bytes to store matrix.
    # Row * Col * 4 bytes total.
    lw t0 0(s1)
    lw t1 0(s2)
    mul t0 t0 t1
    li t1 4
    mul t0 t0 t1
    add s5 t0 zero
    # Malloc memory to store the matrix.
    add a0 s5 zero
    jal malloc
    beq a0 zero malloc_error
    add s3 a0 zero
    # Read matrix.
    add a1 s0 zero
    add a2 s3 zero
    add a3 s5 zero
    jal fread
    bne a0 s5 fread_error

    # Close file.
    add a1 s0 zero
    jal fclose
    bne a0 zero fclose_error

    # Store return argument.
    add a0 s3 zero

    # Epilogue
    lw ra 0(sp)
    lw s0 4(sp)
    lw s1 8(sp)
    lw s2 12(sp)
    lw s3 16(sp)
    lw s4 20(sp)
    lw s5 24(sp)
    addi sp sp 28

    ret

malloc_error:
    li a1 88
    call exit2

fopen_error:
    li a1 89
    call exit2

fread_error:
    li a1 91
    call exit2

fclose_error:
    li a1 90
    call exit2