#include <stdio.h> // for stderr
#include <stdlib.h> // for exit()
#include "types.h"
#include "utils.h"
#include "riscv.h"
#include<math.h>

void execute_rtype(Instruction, Processor *);
void execute_itype_except_load(Instruction, Processor *);
void execute_branch(Instruction, Processor *);
void execute_jal(Instruction, Processor *);
void execute_load(Instruction, Processor *, Byte *);
void execute_store(Instruction, Processor *, Byte *);
void execute_ecall(Processor *, Byte *);
void execute_lui(Instruction, Processor *);
unsigned createMask(unsigned, unsigned);


void execute_instruction(Instruction instruction,Processor *processor,Byte *memory) {
//    printf("PC: %x\n", processor->PC);


    /* YOUR CODE HERE: COMPLETE THE SWITCH STATEMENTS */

    

    switch(instruction.opcode) { // What do we switch on?

            /* YOUR CODE HERE */

        case 0x33:

            //printf("This is R_type\n");

            //printf("Instruction is: %x\n",instruction.bits);

            execute_rtype(instruction, processor);

            processor->PC += 4;


            break;

            

        case 0x03:

            //printf("This is LOAD\n");

            // printf("Instruction is: %d\n",instruction.bits);

            execute_load(instruction, processor,memory);

             processor->PC += 4;

            break;

        case 0x13:

            //printf("This is I_type\n");
//
            // printf("Instruction is: %d\n",instruction.bits);

            execute_itype_except_load(instruction, processor);

             processor->PC += 4;

            break;

        case 0x73:

            //printf("This is ECALL\n");

            //printf("Instruction is: %d\n",instruction.bits);

            execute_ecall(processor, memory);

            break;

            

        case 0x23:

           // printf("This is S_type\n");

            //printf("Instruction is: %d\n",instruction.bits);

            execute_store(instruction, processor, memory);

             processor->PC += 4;

            break;

            

        case 0x63:

          //  printf("This is SB_type\n");

          //  printf("Instruction is: %d\n",instruction.bits);

            execute_branch(instruction, processor);

	    processor->PC += 4;

            break;

        case 0x37:

           // printf("This is U_type\n");

          //  printf("Instruction is: %d\n",instruction.bits);

            execute_lui(instruction, processor);

            processor->PC += 4;

            break;

        case 0x6f:

            //printf("This is UJ_type\n");

            //printf("Instruction is: %d\n",instruction.bits);

            execute_jal(instruction, processor);

            break;

            

        default: // undefined opcode

            printf("INVALID INSTRUCTION\n");

            handle_invalid_instruction(instruction);

            exit(-1);

            break;

            

    }



}

void execute_rtype(Instruction instruction, Processor *processor) {

    switch(instruction.rtype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x00: //check funct3
            if (instruction.rtype.funct7 == 0x00) {
                //ADD: R[rd] ← R[rs1] + R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] + processor->R[instruction.rtype.rs2];
                break;
            }
            if (instruction.rtype.funct7 == 0x01){
                //MUL: R[rd] ← (R[rs1] * R[rs2])[31:0]
                Double full_64bits;
                Word low_bits;
                full_64bits = (processor->R[instruction.rtype.rs1])* (processor->R[instruction.rtype.rs2]);
                low_bits = (Word) full_64bits;
                processor->R[instruction.rtype.rd] = low_bits;
                break;
            }
            if (instruction.rtype.funct7 == 0x20) {
                //SUB: R[rd] ← R[rs1] - R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] - processor->R[instruction.rtype.rs2];
                break;
            }
        case 0x01:
            if (instruction.rtype.funct7 == 0x00) {
                //SLL: R[rd] ← R[rs1] << R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] << processor->R[instruction.rtype.rs2];
                break;
            }
            if (instruction.rtype.funct7 == 0x01) {
                //MULH: R[rd] ← (R[rs1] * R[rs2])[63:32]
                Double full_64bits;
                Word high_bits;
                full_64bits = (processor->R[instruction.rtype.rs1])* (processor->R[instruction.rtype.rs2]);
                high_bits = (Double) full_64bits >> 32;
                processor->R[instruction.rtype.rd] = high_bits;
                break;
            }
        case 0x02:
            if (instruction.rtype.funct7 == 0x00) {
                //SLT: R[rd] ← (R[rs1] < R[rs2]) ? 1 : 0
                if(processor->R[instruction.rtype.rs1] < processor->R[instruction.rtype.rs2])
                    processor->R[instruction.rtype.rd] = 0;
                else
                    processor->R[instruction.rtype.rd] = 1;
                break;
            }
        case 0x04:
            if (instruction.rtype.funct7 == 0x00) {
                //XOR:R[rd] ← R[rs1] ^ R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] ^ processor->R[instruction.rtype.rs2];
                break;
            }
            if (instruction.rtype.funct7 == 0x01) {
                //DIV: R[rd] ← R[rs1] / R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] / processor->R[instruction.rtype.rs2];
                break;
            }
        case 0x05:
            //NEED TO VERIFY BEFORE TEST
            if (instruction.rtype.funct7 == 0x00) {
                //SRL : R[rd] ← R[rs1] >> R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] >> processor->R[instruction.rtype.rs2];
                break;
            }
            if (instruction.rtype.funct7 == 0x20) {
                //SRA: R[rd] ← R[rs1] >> R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] >> processor->R[instruction.rtype.rs2];
                break;
            }

        case 0x06:
            if (instruction.rtype.funct7 == 0x00) {
                //OR: R[rd] ← R[rs1] | R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] | processor->R[instruction.rtype.rs2];
                break;
            }
            if (instruction.rtype.funct7 == 0x01) {
                //REM: R[rd] ← (R[rs1] % R[rs2
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] % processor->R[instruction.rtype.rs2];
                break;
            }
        case 0x07:
            if (instruction.rtype.funct7 == 0x00) {
                //AND: R[rd] ← R[rs1] & R[rs2]
                processor->R[instruction.rtype.rd] =
                        processor->R[instruction.rtype.rs1] & processor->R[instruction.rtype.rs2];
                break;
            }
        default:
            handle_invalid_instruction(instruction);
          //  exit(-1);
            break;
    }

}

void execute_itype_except_load(Instruction instruction, Processor *processor) {
    int shiftOp;
    shiftOp = -1;
    switch(instruction.itype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x0:{
            // ADDI: R[rd] ← R[rs1] + imm
            int bit_length = 12;
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] + imm;
            } else {
                //Convert imm into signed number
                imm = imm | (shiftOp << bit_length);
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] + imm;
            }
            break;
        }
         case 0x1:{

            unsigned imm = instruction.itype.imm;

            unsigned shamt = imm & createMask(0,4);
            processor->R[instruction.itype.rd] =  (processor->R[instruction.itype.rs1]) << shamt;

            break;
        }

            

        case 0x2:{

            int bit_length = 12;

            unsigned int imm = instruction.itype.imm;

            if ((processor->R[instruction.itype.rs1] >> 31) % 2 == 1 && imm >> 12 % 2 == 0) {

                processor->R[instruction.itype.rd] = 1;

                break;

            }

            if (imm < pow(2, bit_length -1)){

                if(processor->R[instruction.itype.rs1] < imm )

                    processor->R[instruction.itype.rd] = 1;

                else

                    processor->R[instruction.itype.rd] = 0;
            } else {

                imm = imm | (shiftOp << bit_length);

                if(processor->R[instruction.itype.rs1] < imm )

                    processor->R[instruction.itype.rd] = 1;

                else

                    processor->R[instruction.itype.rd] = 0;

            }

            break;

        }
        case 0x4:{
            // XORI: R[rd] ← R[rs1] ^ imm
            int bit_length = 12;
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length -1)){
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] ^ imm;

            } else {
                // Convert imm to signed
                imm =  imm | (shiftOp << bit_length);
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] ^ imm;
            }
            break;
        }
        case 0x5:{
            int imm = instruction.itype.imm;
            unsigned shamt = imm & createMask(0,4);
            unsigned func7 = (imm & createMask(5,11))>>5;
            //printf("This is shamt: %d",shamt);
            //printf("This is funct7: %d",func7);

            if(func7 == 0x00){
                // SRLI: R[rd] ← R[rs1] >> imm
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] >> shamt;
            }
            if(func7 == 0x20){
                // SRAI: R[rd] ← R[rs1] >> imm
                // shift rs1 >> shamt and insert the high-order sign bit into empty bits
                int bit_32 =  get_sign(processor->R[instruction.itype.rs1]);
                if (bit_32 == 0){
                    processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] >> shamt;
                }
                else {
                    processor->R[instruction.itype.rd] = (processor->R[instruction.itype.rs1] >> shamt) | createMask(31 - shamt, 31);
                }

            }
            break;
        }
        case 0x6: {
            // ORI: R[rd] ← R[rs1] | imm
            int bit_length = 12;
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length - 1)) {
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] | imm;

            } else {
                // Convert imm to signed
                imm = imm | (shiftOp << bit_length);
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] | imm;
            }
            break;
        }
        case 0x7: {
            // ANDI: R[rd] ← R[rs1] & imm
            int bit_length = 12;
            int imm = instruction.itype.imm;
            if (imm < pow(2, bit_length - 1)) {
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] & imm;

            } else {
                // Convert imm to signed
                imm = imm | (shiftOp << bit_length);
                processor->R[instruction.itype.rd] = processor->R[instruction.itype.rs1] & imm;
            }
            break;
        }

        default:
            handle_invalid_instruction(instruction);
            break;
    }
}



void execute_ecall(Processor *p, Byte *memory) {

    switch(p->R[10]) {

        case 0x1:
            printf("%d", p->R[11]);

            p->PC += 4;

            break;

        case 0xa:

            printf("exiting the simulator\n");

            exit(0);

            break;

        default: // undefined ecall

            printf("Illegal ecall number %d\n", -1); // What stores the ecall arg?

            exit(-1);

            break;
    }
}

void execute_branch(Instruction instruction, Processor *processor) {

    switch(instruction.sbtype.funct3) { // What do we switch on?
        case 0x0:
            if (processor->R[instruction.sbtype.rs1]
                == processor->R[instruction.sbtype.rs2]) {
                processor->PC += (get_branch_offset(instruction)) - 4;
            }

            break;
        case 0x1:
            if (processor->R[instruction.sbtype.rs1]
                != processor->R[instruction.sbtype.rs2]) {
                processor->PC += (get_branch_offset(instruction)) - 4;
            }

            break;

        default:
            handle_invalid_instruction(instruction);
            exit(-1);
            break;
    }
}

void execute_load(Instruction instruction, Processor *processor, Byte *memory) {

    int shiftOp;

    shiftOp = -1;

    int bit_length = 12;

    int offset = instruction.itype.imm;

    if (offset >= pow(2, bit_length -1)) {

        offset = offset | (shiftOp << bit_length);

    }

    switch(instruction.itype.funct3) { // What do we switch on?


        case 0x0:{

            processor->R[instruction.itype.rd] = get_byte(memory[instruction.itype.rs1 + offset]);

            break;
        }

        case 0x1:{
            Half r = 0;

            r = memory[instruction.itype.rs1 + offset + 1];

            r = r << 8;
            r = r | memory[instruction.itype.rs1 + offset];

            processor->R[instruction.itype.rd] = get_half(r);

            break;

        }

        case 0x2:{

            Word r = 0;

            r = memory[instruction.itype.rs1 + offset + 3];

            r = r << 8;

            r = r | memory[instruction.itype.rs1 + offset + 2];

            r = r << 16;

            r = r | memory[instruction.itype.rs1 + offset + 1];

            r = r << 24;

            r = r | memory[instruction.itype.rs1 + offset];
            processor->R[instruction.itype.rd] = r;

            break;

        }

        default:

            handle_invalid_instruction(instruction);

            break;

    }

}

void execute_store(Instruction instruction, Processor *processor, Byte *memory) {
    int shiftOp;
    shiftOp = -1;
    int bit_length = 12;
    unsigned int imm5 = instruction.stype.imm5;
    //printf("This is imm5: %d\n",imm5);
    unsigned int imm7 = instruction.stype.imm7;
    //printf("This is imm7: %d\n",imm7);
    unsigned int offset = (imm7 << 5) | imm5;
    //printf("This is imm7: %d\n",imm);
    if (offset >= pow(2, bit_length -1)) {
        //imm is positive
        offset = offset | (shiftOp << bit_length);
    }
    switch(instruction.stype.funct3) { // What do we switch on?
        /* YOUR CODE HERE */
        case 0x0: {
            // SB: Mem(R[rs1] + offset) ← R[rs2][7:0]
            Byte first_byte = processor->R[instruction.stype.rs2];
            memory[instruction.stype.rs1 + offset] = first_byte;
            break;
        }
        case 0x1: {
            // SH: Mem(R[rs1] + offset) ← R[rs2][15:0]
            Byte first_byte = processor->R[instruction.stype.rs2];
            memory[instruction.stype.rs1 + offset] = first_byte;
            Byte  second_byte = (processor->R[instruction.stype.rs2]) >> 8;
            memory[instruction.stype.rs1 + offset +1] = second_byte;
            break;
        }
        case 0x2: {
            // SW: Mem(R[rs1] + offset) ← R[rs2]
            Byte first_byte = processor->R[instruction.stype.rs2];
            memory[instruction.stype.rs1 + offset] = first_byte;
            Byte  second_byte = (processor->R[instruction.stype.rs2]) >> 8;
            memory[instruction.stype.rs1 + offset +1] = second_byte;
            Byte third_byte = (processor->R[instruction.stype.rs2]) >> 16;
            memory[instruction.stype.rs1 + offset +2] = third_byte;
            Byte fourth_byte = (processor->R[instruction.stype.rs2]) >> 24;
            memory[instruction.stype.rs1 + offset +3] = fourth_byte;
            break;
        }
        default:
            handle_invalid_instruction(instruction);
          //  exit(-1);
            break;
    }
}

void execute_jal(Instruction instruction, Processor *processor) {
    unsignedint imm = get_jump_offset(instruction)*2;
    int rd = instruction.ujtype.rd;

    processor->R[rd] = processor->PC + 4;
    processor->PC += imm;
}

void execute_lui(Instruction instruction, Processor *processor) {
    int imm = instruction.utype.imm;
    int rd = instruction.utype.rd;

    processor->R[rd] = imm << 12;

}

/* Checks that the address is aligned correctly */
int check(Address address,Alignment alignment) {
    if(address>0 && address < MEMORY_SPACE){
        if(alignment == LENGTH_BYTE){
            return 1;
        }
        else if( alignment == LENGTH_HALF_WORD ){
            return address%2 == 0;
        }
        else if (alignment == LENGTH_WORD){
            return address%4 ==0;
        }
    }

    return 0;

}

void store(Byte *memory,Address address,Alignment alignment,Word value, int check_align) {
printf("I am saving a value at %x: %x\n", memory[address], value);
    if((check_align && !check(address,alignment)) || (address >= MEMORY_SPACE)) {
        handle_invalid_write(address);
    }

    if(alignment == LENGTH_BYTE) {

        memory[address] = value;
    }
    if(alignment == LENGTH_HALF_WORD){
        memory[address+1] = value;
	memory[address] = value >> 8;

    }
    if(alignment == LENGTH_WORD){


        memory[address+3] = value;
	memory[address+2] = value >> 8;
	memory[address+1] = value >> 16;
	memory[address] = value >> 24;
    }
}


Word load(Byte *memory,Address address,Alignment alignment, int check_align) {

    if((check_align && !check(address,alignment)) || (address >= MEMORY_SPACE)) {

        printf("CHECK LOAD FUNCTION");

        handle_invalid_read(address);

    }

   
    uint32_t data = 0; // initialize our return value to zero

    

    if(alignment == LENGTH_BYTE) {
        data = memory[address];

    }

    

    if(alignment == LENGTH_HALF_WORD){
        data += memory[address + 1];

        data = data << 8;

        data += memory[address];

    }

    if(alignment == LENGTH_WORD){

        data += memory[address + 3];

//        printf("The 1st byte of WORD: %x\n", memory[address + 3]);

        data = data << 8;

        data += memory[address + 2];

//        printf("The 2nd byte of WORD: %x\n", memory[address + 2]);

        data = data << 8;

        data += memory[address + 1];

//        printf("The 3rd byte of WORD: %x\n", memory[address + 1]);

        data = data << 8;

        data += memory[address];

//        printf("The 4th byte of WORD: %x\n", memory[address]);

//        printf("The INSTRUCTION: %x\n", data);

    }

    return data;

}
