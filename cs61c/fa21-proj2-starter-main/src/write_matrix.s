.globl write_matrix

.text
# ==============================================================================
# FUNCTION: Writes a matrix of integers into a binary file
# FILE FORMAT:
#   The first 8 bytes of the file will be two 4 byte ints representing the
#   numbers of rows and columns respectively. Every 4 bytes thereafter is an
#   element of the matrix in row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is the pointer to the start of the matrix in memory
#   a2 (int)   is the number of rows in the matrix
#   a3 (int)   is the number of columns in the matrix
# Returns:
#   None
# Exceptions:
# - If you receive an fopen error or eof,
#   this function terminates the program with error code 89
# - If you receive an fclose error or eof,
#   this function terminates the program with error code 90
# - If you receive an fwrite error or eof,
#   this function terminates the program with error code 92
# ==============================================================================
write_matrix:

    # Prologue
    addi sp sp -24
    sw ra 0(sp)
    sw a1 4(sp)
    sw a2 8(sp)
    sw a3 12(sp)
    sw s0 16(sp)
    sw s1 20(sp)

    # Open file.
    add a1 a0 zero
    li a2 1
    jal fopen
    li t0 -1
    beq a0 t0 fopen_error
    add s0 a0 zero # s0 stores file descriptor.

    ## Write row and col first.
    # Put row and col to stack memory.
    lw t0 8(sp) # Load row.
    lw t1 12(sp) # Load col.
    addi sp sp -8
    sw t0 0(sp)
    sw t1 4(sp)
    # Call fwrite.
    add a1 s0 zero
    add a2 sp zero
    li a3 2
    li a4 4
    jal fwrite
    li t0 2
    bne a0 t0 fwrite_error
    addi sp sp 8

    ## Write matrix
    lw t0 8(sp)
    lw t1 12(sp)
    mul s1 t0 t1 # Amount of items of the matrix to write to file.
    add a1 s0 zero
    lw a2 4(sp)
    add a3 s1 zero
    li a4 4
    jal fwrite
    bne a0 s1 fwrite_error

    # Close file.
    add a1 s0 zero
    jal fclose
    bne a0 zero fclose_error

    # Epilogue
    lw ra 0(sp)
    lw s0 16(sp)
    lw s1 20(sp)
    addi sp sp 24

    ret

fopen_error:
    li a1 89
    call exit2

fwrite_error:
    li a1 92
    call exit2

fclose_error:
    li a1 90
    call exit2