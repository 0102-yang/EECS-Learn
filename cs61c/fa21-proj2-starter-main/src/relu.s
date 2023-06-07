.globl relu

.text
# ==============================================================================
# FUNCTION: Performs an inplace element-wise ReLU on an array of ints
# Arguments:
# 	a0 (int*) is the pointer to the array
#	a1 (int)  is the # of elements in the array
# Returns:
#	None
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 57
# ==============================================================================
relu:

    li t0 1
    blt a1 t0 length_error

    # Prologue
    addi sp sp -16
    sw ra 0(sp)
    sw s0 4(sp)
    sw s1 8(sp)
    sw s2 12(sp)

    add s0 a0 zero # Pointer to array.
    add s1 a1 zero # Length of array.
    li s2 0 # i.

loop:
    bge s2 s1 loop_end
    lw a0 0(s0)
    jal max
    sw a0 0(s0)
    
    # Update arguments.
    addi s0 s0 4
    addi s2 s2 1

    j loop

loop_end:
    # Epilogue
	lw ra 0(sp)
    lw s0 4(sp)
    lw s1 8(sp)
    lw s2 12(sp)
    addi sp sp 16

	ret

max:
	bge a0 zero max_end
    mv a0 zero
max_end:
	ret

length_error:
    li a1 57
    call exit2