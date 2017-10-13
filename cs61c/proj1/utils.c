#include "utils.h"
#include <stdio.h>
#include <stdlib.h>

unsigned createMask(unsigned, unsigned);
unsigned int get_byte(Byte num);
unsigned int get_half(Half num);
int get_sign(unsigned int num);

/* Helper function to mask from bit a to bit b*/
unsigned createMask(unsigned a, unsigned b) {
    unsigned r = 0;
unsigned i;
    for ( i=a; i<=b; i++)
        r |= 1 << i;
    return r;
}
/* Get a byte from BYTE and sign extend */
unsigned int get_byte(Byte num){
    unsigned int r = 0;

    int sign = ((1 << 7) & num ) >> 7;
    if (sign == 0){
        r = r | num;
    }
    else {
        r = 0xffffff00 | num;
    }
    return r;
}
/* Get HALF WORD ( 2 BYTES) from HALF and sign extend */
unsigned int get_half(Half num){
    unsigned int r = 0;

    int sign = ((1 << 15) & num ) >> 15;
    if (sign == 0){
        r = r | num;
    }
    else {
        r = 0xffff0000 | num;
    }
    return r;
}
/* Helper function gets sign of unsigned int num*/
int get_sign(unsigned int num){
    return ((1 << 31) & num ) >> 31;
}

//sign extends a bitfield with given size
/* You may find implementing this function helpful */
int bitSigner( unsigned int field, unsigned int size){
    /* YOUR CODE HERE */
    return (field >> size) % 16 ;
}

/* Remember that the offsets should return the offset in BYTES */

int get_branch_offset(Instruction instruction) {

    unsigned int imm5 = instruction.sbtype.imm5;
    unsigned int imm7 = instruction.sbtype.imm7;

    unsigned int imm = imm5 + (imm7 << 5);

    unsigned int t11 = imm % 2;
    unsigned int t12 = (imm >> 11) % 2;

    t11 = t11 << 10;
    t12 = t12 << 11;

    imm = imm >> 1;

    imm = (imm ^ t11) + t11;
    imm += t12;

    if ((t12 >> 11) % 2 == 1) {
        printf("im a neg");
        unsigned int temp = 0xFFF;
        imm = (temp ^ imm) + 0x1;
        return imm * -2;
    }

    return imm * 2;
}

int get_jump_offset(Instruction instruction) {
    /* YOUR CODE HERE */

    unsigned int imm = instruction.ujtype.imm;

    unsigned int imm1;
    unsigned int imm3;
    unsigned int imm4;
    unsigned int imm5;

    imm1 = (imm >> 16) % 16;
    imm3 = (imm >> 8) % 16;
    imm4 = (imm >> 4) % 16;
    imm5 = imm % 16;

    unsigned int t1 = 0;
    unsigned int t2 = 0;

    if (imm1 >= 8) {
        t1 = 1;
    }

    if (imm3 % 2 == 1) {
        t2 = 1;
    }

    imm = imm >> 9;

    t2 = t2 << 10;

    imm = (imm ^ t2) + t2;

    imm4 = imm4 << 15;

    imm5 = imm5 << 11;

    imm = imm + imm4 + imm5;

    t1 = t1 << 19;

    imm += t1;

    if ((imm >> 19) % 2 == 1) {
        unsigned int temp = 0xfffff;
        imm = (imm ^ temp) +0x1;
        return imm * -2;
    }

    return imm*2;
}

int get_store_offset(Instruction instruction) {
    /* YOUR CODE HERE */
    return 0;
}

void handle_invalid_instruction(Instruction instruction) {
    printf("Invalid Instruction: 0x%08x\n", instruction.bits); 
}

void handle_invalid_read(Address address) {
    printf("Bad Read. Address: 0x%08x\n", address);
    exit(-1);
}

void handle_invalid_write(Address address) {
    printf("Bad Write. Address: 0x%08x\n", address);
    exit(-1);
}
