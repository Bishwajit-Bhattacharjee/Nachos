#include "syscall.h"

int main () {
    int i, j, n;
    char b[10];
    printf("Enter a num: ");
    readline(b, 10);
    n = atoi(b);

    for (i = 1; i <= n; i++) {
        for (j = 1; j <= i; j++) printf("*");
        printf("\n");
    }
}