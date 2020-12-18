#include "syscall.h"

int main()
{
    int fibo[1000];
    fibo[0] = 1;
    fibo[1] = 1;
    int i;

    for (i = 2; i < 1000; i++)
    {
        fibo[i] = (fibo[i-1] + fibo[i-2]) % 1000000007;
    }

    return fibo[9];


}