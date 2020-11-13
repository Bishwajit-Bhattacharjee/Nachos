#include "syscall.h"

int main()
{
    int status, processID, retVal;
    char *execArgs[6];

    execArgs[0] = "", execArgs[1] = "";

    printf("Calling exec on 1st child\n");
    processID = exec("my_exit_child.coff", 1,  execArgs);
    retVal = join(processID, &status);
    printf("1st child exited with status %d\n", status);

    printf("Calling exec on 2nd child\n");
    processID = exec("my_exit_child.coff", 2,  execArgs);
    retVal = join(processID, &status);
    printf("2nd child exited with status %d\n", status);

    printf("Calling exec on 3rd child\n");
    processID = exec("my_exit_child.coff", 3,  execArgs);
    retVal = join(processID, &status);
    printf("3rd child exited with status %d\n", status);

    return 0;
}
