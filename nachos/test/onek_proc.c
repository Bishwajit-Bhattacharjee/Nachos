#include "stdio.h"
#include "syscall.h"

int main() {
    int i, j, status, pID;
    char *execArgs[256];

    for (i = 0; i < 20 ; i++) {
        printf("Process num %d is starting \n", i + 1);

        pID = exec("halt.coff", 1, execArgs);
        //status = join(pID, &status);

        printf("Process num %d is ending with status %d\n", i + 1, status);
    }
    return 0;
}