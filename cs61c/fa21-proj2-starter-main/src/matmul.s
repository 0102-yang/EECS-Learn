.globl matmul

.data
m0: .word 1 2 3 4 5 6 7 8 9
m1: .word 1 2 3 4 5 6 7 8 9
m2: .word 0 0 0 0 0 0 0 0 0

.text
# =======================================================
# FUNCTION: Matrix Multiplication of 2 integer matrices
# 	d = matmul(m0, m1)
# Arguments:
# 	a0 (int*)  is the pointer to the start of m0
#	a1 (int)   is the # of rows (height) of m0
#	a2 (int)   is the # of columns (width) of m0
#	a3 (int*)  is the pointer to the start of m1
# 	a4 (int)   is the # of rows (height) of m1
#	a5 (int)   is the # of columns (width) of m1
#	a6 (int*)  is the pointer to the the start of d
# Returns:
#	None (void), sets d = matmul(m0, m1)
# Exceptions:
#   Make sure to check in top to bottom order!
#   - If the dimensions of m0 do not make sense,
#     this function terminates the program with exit code 59
#   - If the dimensions of m1 do not make sense,
#     this function terminates the program with exit code 59
#   - If the dimensions of m0 and m1 don't match,
#     this function terminates the program with exit code 59
# =======================================================
matmul:
	li t0 1
    blt a1 t0 error_handle
    blt a2 t0 error_handle
    blt a4 t0 error_handle
    blt a5 t0 error_handle
    bne a2 a4 error_handle

    # Prologue
    addi sp sp -16
    sw s0 0(sp)
    sw s1 4(sp)
    sw s2 8(sp)
    sw ra 12(sp)
    
    mv s0 a3
    mv s1 zero
    mv s2 zero

outer_loop_start:
	bge s1 a1 outer_loop_end
    # Place the start of matrix1.
    mv s0 a3
    mv s2 zero

inner_loop_start:
	bge s2 a5 inner_loop_end
     
    # Save args.
    addi sp sp -24
    sw a0 0(sp)
    sw a1 4(sp)
    sw a2 8(sp)
    sw a3 12(sp)
    sw a4 16(sp)
    sw a5 20(sp)
    
    mv a1 s0
    li a3 1
    mv a4 a5
    jal dot
    mv t0 a0
    
    # Store args.
    lw a0 0(sp)
    lw a1 4(sp)
    lw a2 8(sp)
    lw a3 12(sp)
    lw a4 16(sp)
    lw a5 20(sp)
    addi sp sp 24
    
    sw t0 0(a6)
    
    # Update args.
    addi s0 s0 4
    addi s2 s2 1
    addi a6 a6 4
    j inner_loop_start

inner_loop_end:
	mv s0 a3
	addi s1 s1 1
    # Update address of matrix1.
    li t1 2
    mv t0 a2
    sll t0 t0 t1
    add a0 a0 t0
    j outer_loop_start

outer_loop_end:
    # Epilogue
	lw s0 0(sp)
    lw s1 4(sp)
    lw s2 8(sp)
    lw ra 12(sp)
    addi sp sp 16
    
    ret

error_handle:
	li a1 59
    call exit2

# dot function
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