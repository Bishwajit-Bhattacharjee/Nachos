#include "syscall.h"

int main(int argc, char** argv)
{
    int i;
    for(i = 0; i < 30; i++) {
        printf("%s%d\n", argv[0], i);
    }

    return 0;
}
