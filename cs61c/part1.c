#include <stdio.h> // for stderr
#include <stdlib.h> // for exit()
#include "types.h"
#include "utils.h"
#include "string.h"
#include<math.h>


void print_rtype(char *, Instruction);
void print_itype_except_load(char *, Instruction, int);
void print_load(char *, Instruction);
void print_store(char *, Instruction);
void print_branch(char *, Instruction);
void write_rtype(Instruction);
void write_itype_except_load(Instruction); 
void write_load(Instruction);
void write_store(Instruction);
void write_branch(Instruction);
void write_lui(Instruction);
void write_jal(Instruction);
void write_ecall(Instruction);

unsigned get_bit(unsigned x,
                 unsigned n) {

    return (x >> n) % 2;
}

// Set the nth bit of the value of x to v.
// Assume 0 <= n <= 31, and v is 0 or 1
void set_bit(unsigned *x,
             unsigned n,
             unsigned v) {
    // YOUR CODE HERE
    int temp = get_bit(*x, n);

    temp = temp << n;

    *x= (*x ^ temp) + (v << n);
}



void decode_instruction(Instruction instruction) {
    /* YOUR CODE HERE: COMPLETE THE SWITCH STATEMENTS */
    switch(instruction.opcode) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x33:
            write_rtype(instruction);
            break;

        case 0x03:
            write_load(instruction);
            break;
        case 0x13:
            write_itype_except_load(instruction);
            break;
        case 0x73:
          //  printf("This is I_type_end_call\n");
            write_ecall(instruction);
            break;

        case 0x23:
           // printf("This is S_type\n");
            write_store(instruction);
            break;

        case 0x63:
          //  printf("This is SB_type\n");
            write_branch(instruction);
            break;
        case 0x37:
           // printf("This is U_type\n");
            write_lui(instruction);
            break;
        case 0x6f:
           // printf("This is UJ_type\n");
            write_jal(instruction);
            break;

       default: // undefined opcode
            handle_invalid_instruction(instruction);
           break;
    }
}

void write_rtype(Instruction instruction) {
    //char name[20]; // DO I DECLARE ARRAY OF CHAR OR CHAR* ?????
   switch(instruction.rtype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x0: //check funct3
            if (instruction.rtype.funct7 == 0x00){
                char op[] = "add";
                print_rtype(op, instruction); // CALL print_rtype
                break;

            }
            else if(instruction.rtype.funct7 == 0x01){
                char op[] = "mul";
                print_rtype(op, instruction); // CALL print_rtype
                break;
            }
            else if(instruction.rtype.funct7 == 0x20){
                char op[] = "sub";
                print_rtype(op, instruction); // CALL print_rtype
                break;
            }
        case 0x01: //check funct3
            if (instruction.rtype.funct7 == 0x00){
                char op[] = "sll";
                print_rtype(op, instruction); // CALL print_rtype
                break;

            }
            else if(instruction.rtype.funct7 == 0x01){
                char op[] = "mulh";
                print_rtype(op, instruction); // CALL print_rtype
                break;
            }
        case 0x02: {
            char op[] = "slt";
            print_rtype(op, instruction); // CALL print_rtype
            break;
        }
        case 0x04: //check funct3
            if (instruction.rtype.funct7 == 0x00){
                char op[] = "xor";
                print_rtype(op, instruction); // CALL print_rtype
                break;

            }
            else if(instruction.rtype.funct7 == 0x01){
                char op[] = "div";
                print_rtype(op, instruction); // CALL print_rtype
                break;
            }
        case 0x05: //check funct3
            if (instruction.rtype.funct7 == 0x00){
                char op[] = "srl";
                print_rtype(op, instruction); // CALL print_rtype
                break;

            }
            else if(instruction.rtype.funct7 == 0x20){
                char op[] = "sra";
                print_rtype(op, instruction); // CALL print_rtype
                break;
            }
        case 0x06: //check funct3
            if (instruction.rtype.funct7 == 0x00){
                char op[] = "or";
                print_rtype(op, instruction); // CALL print_rtype
                break;

            }
            else if(instruction.rtype.funct7 == 0x01){
                char op[] = "rem";
                print_rtype(op, instruction); // CALL print_rtype
                break;
            }
        case 0x07: {
            char op[] = "and";
            print_rtype(op, instruction); // CALL print_rtype
            break;
        }

       default:
            handle_invalid_instruction(instruction);
            break;
   }
}

void write_itype_except_load(Instruction instruction) {
    int shiftOp;
    shiftOp = -1;
    switch(instruction.itype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x0: {
            int bit_length = 12;
            char op[] = "addi";
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm); // CALL print_rtype

            } else {
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm | (shiftOp << bit_length));
            }
            break;
        }
        case 0x1: {
            char op[] = "slli";
            int imm = instruction.itype.imm;
            unsigned shamt = imm & createMask(0,4);
            //unsigned func7 = imm & createMask(5,11);
            //printf("This is shamt: %d",shamt);
            //printf("This is funct7: %d",func7);
            print_itype_except_load(op, instruction, shamt); // CALL print_rtype
            break;

        }
        case 0x2: {
            int bit_length = 12;
            char op[] = "slti";
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm); // CALL print_rtype

            } else {
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm | (shiftOp << bit_length));
            }
            break;

        }
        case 0x4: {
            int bit_length = 12;
            char op[] = "xori";
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm); // CALL print_rtype

            } else {
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm | (shiftOp << bit_length));
            }
            break;
        }
        case 0x5: {
            int imm = instruction.itype.imm;
            unsigned shamt = imm & createMask(0,4);
            unsigned func7 = (imm & createMask(5,11))>>5;
            //printf("This is shamt: %d",shamt);
            //printf("This is funct7: %d",func7);

            if(func7 == 0x00){
                char op[] = "srli";
                print_itype_except_load(op, instruction, shamt);
            }
            if(func7 == 0x20){
                char op[] = "srai";
                print_itype_except_load(op, instruction, shamt);
            }
            break;
        }
        case 0x6: {
            int bit_length = 12;
            char op[] = "ori";
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm); // CALL print_rtype

            } else {
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm | (shiftOp << bit_length));
            }
            break;
        }
        case 0x7: {
            int bit_length = 12;
            char op[] = "andi";
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm); // CALL print_rtype

            } else {
                //printf("This is imm: %d",imm);
                print_itype_except_load(op, instruction, imm | (shiftOp << bit_length));
            }
            break;
        }


        default:
            handle_invalid_instruction(instruction);
            break;  
    }
}

void write_load(Instruction instruction) {
    switch(instruction.rtype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x0:{
            char op[] = "lb";
            print_load(op, instruction); // CALL print_load
            break;
        }
        case 0x1:{
            char op[] = "lh";
            print_load(op, instruction); // CALL print_load
            break;
        }
        case 0x2:{
            char op[] = "lw";
            print_load(op, instruction); // CALL print_load
            break;
        }
        default:
            handle_invalid_instruction(instruction);
            break;
    }
}

void write_store(Instruction instruction) {
    switch(instruction.stype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x0: {
            char op[] = "sb";
            print_store(op, instruction);
            break;
        }
        case 0x1: {
            char op[] = "sh";
            print_store(op, instruction);
            break;
        }
        case 0x2: {
            char op[] = "sw";
            print_store(op, instruction);
            break;
        }


        default:
            handle_invalid_instruction(instruction);
            break;
    }
}

void write_branch(Instruction instruction) {
    switch(instruction.sbtype.funct3) { // What do we switch on?
        case 0x0:
            print_branch("beq", instruction);
            break;

        case 0x1:
            print_branch("bne", instruction);
            break;
        default:
            handle_invalid_instruction(instruction);
            break;
    }
}

/* For the writes, probably a good idea to take a look at utils.h */

void write_lui(Instruction instruction) {
    int rd = instruction.utype.rd;
    int imm = instruction.utype.imm;

    printf(LUI_FORMAT, rd, imm);

}

void write_jal(Instruction instruction) {
    int rd = instruction.ujtype.rd;

    int imm = get_jump_offset(instruction);

    printf(JAL_FORMAT, rd, imm);

}

void write_ecall(Instruction instruction) {
    /* YOUR CODE HERE */
    printf(ECALL_FORMAT);
}

void print_rtype(char *name, Instruction instruction) {
   /*
    HOW CAN I PRINT THE WHOLE NAME ///

*/
    /* YOUR CODE HERE */
    int rd = instruction.rtype.rd;
    int rs1 = instruction.rtype.rs1;
    int rs2 = instruction.rtype.rs2;
    printf(RTYPE_FORMAT,name, rd, rs1, rs2);
}

void print_itype_except_load(char *name, Instruction instruction, int imm) {
    /* YOUR CODE HERE */
    int rd = instruction.itype.rd;
    int rs1 = instruction.itype.rs1;
    printf(ITYPE_FORMAT,name, rd, rs1, imm);
}

void print_load(char *name, Instruction instruction) {
    /* YOUR CODE HERE */
    int shiftOp;
    shiftOp = -1;
    int bit_length = 12;

    int rd = instruction.itype.rd;
    int rs1 = instruction.itype.rs1;
    int imm = instruction.itype.imm;
    if (imm < pow(2, bit_length -1)){
        //imm is positive
        printf(MEM_FORMAT,name,rd,imm,rs1); //

    } else {
        //imm is negative
        printf(MEM_FORMAT,name,rd,imm | (shiftOp << bit_length),rs1);
    }


}

void print_store(char *name, Instruction instruction) {
    /* YOUR CODE HERE */
    int shiftOp;
    shiftOp = -1;
    int bit_length = 12;
    int rs1= instruction.stype.rs1;
    int rs2 = instruction.stype.rs2;
    unsigned int imm5 = instruction.stype.imm5;
    //printf("This is imm5: %d\n",imm5);
    unsigned int imm7 = instruction.stype.imm7;
    //printf("This is imm7: %d\n",imm7);
    unsigned int imm = (imm7 << 5) | imm5;
    //printf("This is imm7: %d\n",imm);
    if (imm < pow(2, bit_length -1)){
        //imm is positive
        printf(MEM_FORMAT,name,rs2,imm,rs1); //

    } else {
        //imm is negative
        printf(MEM_FORMAT,name,rs2,imm | (shiftOp << bit_length),rs1);
    }

}

void print_branch(char *name, Instruction instruction) {

    int rs1 = instruction.sbtype.rs1;
    int rs2 = instruction.sbtype.rs2;
    int imm = get_branch_offset(instruction);
    printf(BRANCH_FORMAT, name, rs1, rs2, imm);
}

