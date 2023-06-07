.globl dot

.text
# =======================================================
# FUNCTION: Dot product of 2 int vectors
# Arguments:
#   a0 (int*) is the pointer to the start of v0
#   a1 (int*) is the pointer to the start of v1
#   a2 (int)  is the length of the vectors
#   a3 (int)  is the stride of v0
#   a4 (int)  is the stride of v1
# Returns:
#   a0 (int)  is the dot product of v0 and v1
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 57
# - If the stride of either vector is less than 1,
#   this function terminates the program with error code 58
# =======================================================
dot:
    li t0 1
    blt a2 t0 length_error
    blt a3 t0 stride_error
    blt a4 t0 stride_error

    # Prologue
    addi sp sp -20
    sw s0 0(sp) # i.
    sw s1 4(sp) # Stride bytes of vector1.
    sw s2 8(sp) # Stride bytes of vector2.
    sw s3 12(sp) # Length of each vector.
    sw s4 16(sp) # Result.

    li s0 0
    li t0 4
    mul s1 a3 t0
    mul s2 a4 t0
    add s3 a2 zero
    li s4 0

loop_start:
    bge s0 s3 loop_end
    lw t0 0(a0)
    lw t1 0(a1)
    mul t0 t0 t1
    add s4 s4 t0
    # Update arguments.
    add a0 a0 s1
    add a1 a1 s2
    addi s0 s0 1
    j loop_start

loop_end:
    add a0 s4 zero
    # Epilogue
    lw s0 0(sp)
    lw s1 4(sp)
    lw s2 8(sp)
    lw s3 12(sp)
    lw s4 16(sp)
    addi sp sp 20

    ret

length_error:
    li a1 57
    call exit2

stride_error:
    li a1 58
    call exit2
