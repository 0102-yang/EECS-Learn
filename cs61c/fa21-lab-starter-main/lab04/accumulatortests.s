.import lotsofaccumulators.s

.data
inputarray: .word 0,1,2,3,4,5,6,7,0

TestPassed: .asciiz "Test Passed!"
TestFailed: .asciiz "Test Failed!"

.text
# Tests if the given implementation of accumulate is correct.
#Input: a0 contains a pointer to the version of accumulate in question. See lotsofaccumulators.s for more details
#
#
#
#The main function currently runs a simple test that checks if accumulator works on the given input array. All versions of accumulate should pass this.
#Modify the test so that you can catch the bugs in four of the five solutions!
main:
	addi sp sp -8
    li s2 1234
    sw s2 4(sp)

	li s0 99
	mv s1 s0
    la a0 inputarray
    li t2 1
    mv t4 sp
    jal accumulatorthree
    bne sp t4 Fail
    li t0 28
    bne s1 s0 Fail

    lw s1 4(sp)
    addi sp sp 8
    bne s1 s2 Fail
    beq a0 t0 Pass
Fail:
    la a0 TestFailed
    jal print_string
    j End
Pass:
    la a0 TestPassed
    jal print_string
End:
    jal exit

print_int:
	mv a1 a0
    li a0 1
    ecall
    jr ra
    
print_string:
	mv a1 a0
    li a0 4
    ecall
    jr ra
    
exit:
    li a0 10
    ecall