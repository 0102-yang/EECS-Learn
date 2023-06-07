.globl classify

.text
classify:
    # =====================================
    # COMMAND LINE ARGUMENTS
    # =====================================
    # Args:
    #   a0 (int)    argc
    #   a1 (char**) argv
    #   a2 (int)    print_classification, if this is zero,
    #               you should print the classification. Otherwise,
    #               this function should not print ANYTHING.
    # Returns:
    #   a0 (int)    Classification
    # Exceptions:
    # - If there are an incorrect number of command line args,
    #   this function terminates the program with exit code 72
    # - If malloc fails, this function terminates the program with exit code 88
    #
    # Usage:
    #   main.s <M0_PATH> <M1_PATH> <INPUT_PATH> <OUTPUT_PATH>

    # Check number of command line arguments.
    li t0 5
    bne t0 a0 command_line_argument_error

    # Prologue.
    addi sp sp -88
    sw ra 0(sp)
    # Pointer of M0 matrix 4(sp).
    # Pointer of M1 matrix 8(sp).
    # Pointer of INPUT matrix 12(sp).
    # Pointer of h matrix 16(sp).
    # Row of M0 matrix 20(sp)
    # Col of M0 matrix 24(sp)
    # Row of M1 matrix 28(sp)
    # Col of M1 matrix 32(sp)
    # Row of INPUT matrix 36(sp)
    # Col of INPUT matrix 40(sp)
    lw t0 4(a1)
    sw t0 44(sp) # M0_PATH
    lw t0 8(a1)
    sw t0 48(sp) # M1_PATH
    lw t0 12(a1)
    sw t0 52(sp) # INPUT_PATH
    lw t0 16(a1)
    sw t0 56(sp) # OUTPUT_PATH
    sw a2 60(sp) # Print_classification
    # Pointer of o matrix 64(sp).
    # Row of o matrix 68(sp).
    # Col of o matrix 72(sp).
    # Row of h matrix 76(sp).
    # Col of h matrix 80(sp).
    # Classification 84(sp).

	# =====================================
    # LOAD MATRICES
    # =====================================
    # Load pretrained m0
    lw a0 44(sp)
    addi a1 sp 20
    addi a2 sp 24
    jal read_matrix
    sw a0 4(sp)

    # Load pretrained m1
    lw a0 48(sp)
    addi a1 sp 28
    addi a2 sp 32
    jal read_matrix
    sw a0 8(sp)

    # Load input matrix
    lw a0 52(sp)
    addi a1 sp 36
    addi a2 sp 40
    jal read_matrix
    sw a0 12(sp)

    # =====================================
    # RUN LAYERS
    # =====================================
    ## 1. LINEAR LAYER:    m0 * input
    # Malloc space for matrix h.
    lw a0 20(sp)
    lw t1 40(sp)
    mul a0 a0 t1
    li t1 4
    mul a0 a0 t1
    jal malloc
    beq a0 zero malloc_error
    sw a0 16(sp)

    # Multiply m0 and input matrix and put result into h matrix.
    lw a0 4(sp)
    lw a1 20(sp)
    sw a1 76(sp)
    lw a2 24(sp)
    lw a3 12(sp)
    lw a4 36(sp)
    lw a5 40(sp)
    sw a5 80(sp)
    lw a6 16(sp)
    jal matmul

    ## 2. NONLINEAR LAYER: ReLU(m0 * input)
    lw a0 16(sp)
    lw a1 76(sp)
    lw t1 80(sp)
    mul a1 a1 t1
    jal relu

    ## 3. LINEAR LAYER:    m1 * ReLU(m0 * input)
    # Malloc space for matrix o.
    lw a0 28(sp)
    lw t0 80(sp)
    mul a0 a0 t0
    li t0 4
    mul a0 a0 t0
    jal malloc
    beq a0 zero malloc_error
    sw a0 64(sp)

    # Multiply matrix m1 and h then put result into matrix o.
    lw a0 8(sp)
    lw a1 28(sp)
    sw a1 68(sp)
    lw a2 32(sp)
    lw a3 16(sp)
    lw a4 76(sp)
    lw a5 80(sp)
    sw a5 72(sp)
    lw a6 64(sp)
    jal matmul

    # =====================================
    # WRITE OUTPUT
    # =====================================
    # Write output matrix
    lw a0 56(sp)
    lw a1 64(sp)
    lw a2 68(sp)
    lw a3 72(sp)
    jal write_matrix

    # =====================================
    # CALCULATE CLASSIFICATION/LABEL
    # =====================================
    # Call argmax
    lw a0 64(sp)
    lw a1 68(sp)
    lw t0 72(sp)
    mul a1 a1 t0
    jal argmax
    sw a0 84(sp)

    # Print classification
    lw t0 60(sp)
    bne t0 zero print_end
    add a1 a0 zero
    jal print_int
    # Print newline afterwards for clarity
    li a1 10 # '\n'
    jal print_char

print_end:
	# Free heap space.
    lw a0 4(sp)
    jal free
    lw a0 8(sp)
    jal free
    lw a0 12(sp)
    jal free
    lw a0 16(sp)
    jal free
    lw a0 64(sp)
    jal free
    
    # Return value.
    lw a0 84(sp) 

    # Epilogue
    lw ra 0(sp)
    addi sp sp 88

    ret

# Matmul function.
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

dot_loop_start:
    bge s0 s3 dot_loop_end
    lw t0 0(a0)
    lw t1 0(a1)
    mul t0 t0 t1
    add s4 s4 t0
    # Update arguments.
    add a0 a0 s1
    add a1 a1 s2
    addi s0 s0 1
    j dot_loop_start

dot_loop_end:
    add a0 s4 zero
    # Epilogue
    lw s0 0(sp)
    lw s1 4(sp)
    lw s2 8(sp)
    lw s3 12(sp)
    lw s4 16(sp)
    addi sp sp 20

    ret

# Argmax function.
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

argmax_loop_start:
    bge s2 s1 argmax_loop_end
    lw t0 0(s0)
    ble t0 s3 argmax_update
    # Update max elements and index.
    add s3 t0 zero
    add s4 s2 zero
argmax_update:
    addi s0 s0 4
    addi s2 s2 1
    j argmax_loop_start

argmax_loop_end:
    add a0 s4 zero

    # Epologue
    lw s0 0(sp)
    lw s1 4(sp)
    lw s2 8(sp)
    lw s3 12(sp)
    lw s4 16(sp)
    addi sp sp 20

    ret

# Relu function.
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

# Read matrix function.
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

# Write matrix function.
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

# Error handle function.
length_error:
    li a1 57
    call exit2

stride_error:
    li a1 58
    call exit2

error_handle:
	li a1 59
    call exit2

command_line_argument_error:
    li a1 72
    call exit2

malloc_error:
    li a1 88
    call exit2

fopen_error:
    li a1 89
    call exit2

fclose_error:
    li a1 90
    call exit2

fread_error:
    li a1 91
    call exit2

fwrite_error:
    li a1 92
    call exit2