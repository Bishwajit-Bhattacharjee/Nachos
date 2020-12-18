#include "syscall.h"

void main()
{


    char *execArgs[256];
    int status1,processID,k;
    processID = exec("dbg_echo.coff", 1,  execArgs);
    k = join(processID, &status1);
    printf("1st process ending\n");

}
