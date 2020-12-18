#include "syscall.h"

void main()
{
    char *execArgs[256];
//    execArgs[0] = "zawad";
    int status1,processID,k;
    processID = exec("halt.coff", 1,  execArgs);
    k = join(processID, &status1);
    printf("2nd process ending\n");
}
