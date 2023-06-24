.globl factorial

.data
n: .word 8

.text
main:
    la t0, n
    lw a0, 0(t0)
    jal ra, factorial

    addi a1, a0, 0
    addi a0, x0, 1
    ecall # Print Result

    addi a1, x0, '\n'
    addi a0, x0, 11
    ecall # Print newline

    addi a0, x0, 10
    ecall # Exit

factorial:
    # YOUR CODE HERE
	add t0, a0, x0
    addi s0, x0, 1
    addi s1, x0, 2 # Initialize
Loop:
    beq t0, x0, Exit
    mul s0, s0, t0
    addi t0, t0, -1
    jal x0, Loop
Exit:
    add a0, s0, x0
    jr ra