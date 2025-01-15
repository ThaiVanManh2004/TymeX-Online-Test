#include <iostream>
#include <fstream>

int main()
{
    std::ifstream file("input.txt");
    if (!file.is_open())
    {
        std::cerr << "Failed to open file." << std::endl;
        return 1;
    }
    // Read the number of elements
    int n;
    file >> n;
    int temp;
    // Sum 1 2 ... (n + 1) = (n + 1) * (n + 2) / 2
    long long sum = (n + 1) * (n + 2) / 2;
    // Read the elements and subtract them from the sum
    for (int i = 0; i < n; ++i)
    {
        file >> temp;
        sum -= temp;
    }
    // The remaining number is the missing number
    std::cout << sum;
}