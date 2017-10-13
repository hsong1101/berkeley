#include "types.h"

#define RTYPE_FORMAT "%s\tx%d, x%d, x%d\n"
#define ITYPE_FORMAT "%s\tx%d, x%d, %d\n"
#define MEM_FORMAT "%s\tx%d, %d(x%d)\n"
#define LUI_FORMAT "lui\tx%d, %d\n"
#define JAL_FORMAT "jal\tx%d, %d\n"
#define BRANCH_FORMAT "%s\tx%d, x%d, %d\n"
#define ECALL_FORMAT "ecall\n"

unsigned int createMask(unsigned, unsigned);
int get_sign(unsigned int num);
unsigned int get_byte(Byte num);
unsigned int get_half(Half num);
int bitSigner(unsigned, unsigned);
int get_branch_offset(Instruction instruction);
int get_jump_offset(Instruction instruction);
int get_store_offset(Instruction);
void handle_invalid_instruction(Instruction);
void handle_invalid_read(Address);
void handle_invalid_write(Address);
