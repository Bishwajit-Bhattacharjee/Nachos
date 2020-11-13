#include "syscall.h"

int main(int argc, char** argv)
{
    if(argc == 1)
        return 269;
    if(argc == 2)
        return 789;

    return 0;
}
