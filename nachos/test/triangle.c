#include "syscall.h"

int main() {
    int n, i, j;
    printf("Please enter a number : \n");
    scanf("%d", &n);
    //n = 5;

    for ( i = 0; i < n; i++) {
        for( j = 0; j <= i; j++) {
            printf("*");
        }
        printf("\n");
    }
    halt();
}