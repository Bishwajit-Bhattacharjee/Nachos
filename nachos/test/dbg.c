#include "syscall.h"

void main()
{


    char *execArgs[256];
    int status[10],processID[10],k,i,n;

    n = 5;
    for (i=0;i<n;i++)
    {
        processID[i] = exec("fibo.coff", 1,  execArgs);
        k = join(processID[i], &status[i]);
    }

//    for (i=0;i<n;i++)
//    {
//
//    }

    for (i=0;i<n;i++)
    {
         printf("%dth itr %d\n", i, status[i]);
    }


}
