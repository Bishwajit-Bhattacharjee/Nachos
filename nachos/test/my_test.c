#include "syscall.h"

int main()
{
    int status1,processID, processID1, processID2, status2, i, k;
    char *execArgs[6];

    for(i = 0; i < 6; i++) execArgs[i] = "";

//    printf("\n\n\n********************************** Multiprogramming tests **********************************\n\n\n");
//
//
//    printf("\n\n********************************** Read-write test **********************************\n\n");
//
//    printf("my_test forking echo.coff and joining... \n");
//    processID = exec("echo.coff", 1,  execArgs);
//    k = join(processID, &status1);
//    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);
//
//    printf("\n\n********************************** Join test **********************************\n\n");
//
//    printf("my_test forking my_join and joining... \n");
//    processID = exec("my_join.coff", 3,  execArgs);
//    k = join(processID, &status1);
//    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);
//
//    printf("\nDoing this to see that page freeing works\n\n");
//
//    printf("my_test forking my_join and joining... \n");
//    processID = exec("my_join.coff", 2,  execArgs);
//    k = join(processID, &status1);
//    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);

    printf("\n\n********************************** Multiple process running concurrently test **********************************\n\n");

    printf("my_test forking my_print.coff and joining... \n");
    processID = exec("my_print.coff", 1,  execArgs);
    k = join(processID, &status1);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);

    printf("\n\n********************************** Exit status test **********************************\n\n");

    printf("my_test forking my_exit and joining... \n");
    processID = exec("my_exit.coff", 1,  execArgs);
    k = join(processID, &status1);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);

    halt();
    printf("Should not reach!!!");

    /* not reached */
}
