.import read_matrix.s
.import write_matrix.s
.import matmul.s
.import dot.s
.import relu.s
.import argmax.s
.import utils.s
.import classify.s

# .data
# m0_path: .asciiz "inputs/simple1/bin/m0.bin"
# m1_path: .asciiz "inputs/simple1/bin/m1.bin"
# input_path: .asciiz "inputs/simple1/bin/inputs/input0.bin"
# output_path: .asciiz "outputs/test_basic_main/student.bin"

.globl main
# .text
# This is a dummy main function which imports and calls the classify function.
# While it just exits right after, it could always call classify again.
main:
#     li a0 20
#     jal malloc
#     la s0 m0_path
#     sw s0 4(a0)
#     la s0 m1_path
#     sw s0 8(a0)
#     la s0 input_path
#     sw s0 12(a0)
#     la s0 output_path
#     sw s0 16(a0)

#     add a1 a0 zero
#     li a0 5
    # initialize register a2 to zero
    mv a2, zero

    # call classify function
    jal classify

    # exit program normally
    jal exit
