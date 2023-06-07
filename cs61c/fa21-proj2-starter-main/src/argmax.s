.globl argmax

.text
# =================================================================
# FUNCTION: Given a int vector, return the index of the largest
#	element. If there are multiple, return the one
#	with the smallest index.
# Arguments:
# 	a0 (int*) is the pointer to the start of the vector
#	a1 (int)  is the # of elements in the vector
# Returns:
#	a0 (int)  is the first index of the largest element
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 57
# =================================================================
argmax:

    li t0 1
    blt a1 t0 length_error

    # Prologue
	addi sp sp -20
    sw s0 0(sp) # Pointer of array.
    sw s1 4(sp) # Length of array.
    sw s2 8(sp) # i.
    sw s3 12(sp) # Max element of array.
    sw s4 16(sp) # The index of max element.

    add s0 a0 zero
    add s1 a1 zero
    li s2 1
    lw s3 0(a0)
    li s4 0
    addi s0 s0 4

loop_start:
    bge s2 s1 loop_end
    lw t0 0(s0)
    ble t0 s3 update
    # Update max elements and index.
    add s3 t0 zero
    add s4 s2 zero
update:
    addi s0 s0 4
    addi s2 s2 1
    j loop_start

loop_end:
    add a0 s4 zero

    # Epologue
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