#include "syscall.h"

void main()
{
    char *execArgs[256];
    int status1,processID,k;
    processID = exec("halt.coff", 1,  execArgs);
    k = join(processID, &status1);
    printf("2nd process ending\n");
}
