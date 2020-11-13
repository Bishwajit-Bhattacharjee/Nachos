#include "syscall.h"

int main()
{
    int status, processID1, processID2, processID3, retVal;
    char *execArgs[6];

    printf("Calling exec 3 children\n");

    execArgs[0] = "A";
    processID1 = exec("my_print_child.coff", 1,  execArgs);

    execArgs[0] = "B";
    processID2 = exec("my_print_child.coff", 1,  execArgs);

    execArgs[0] = "C";
    processID3 = exec("my_print_child.coff", 1,  execArgs);

    retVal = join(processID1, &status);
    retVal = join(processID2, &status);
    retVal = join(processID3, &status);

    return 0;
}
