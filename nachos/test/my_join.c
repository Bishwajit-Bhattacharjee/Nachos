#include "syscall.h"

int main(int argc, char** argv)
{
    int status, processID, retVal;

    printf("Joining %d:\n", argc);

    if(argc > 0) {
        processID = exec("my_join.coff", argc-1,  argv);
        retVal = join(processID, &status);
        printf("Exec on %d on got processID %d\n", argc-1, processID);
    }

    printf("Joined %d\n", argc);

    return 0;
}
