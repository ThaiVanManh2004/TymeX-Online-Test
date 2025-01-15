#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <iomanip> // for std::fixed and std::setprecision
#include <fstream> // for file reading
#include <sstream> // for string stream

class Product
{
public:
    std::string name;
    double price;
    int quantity;

    Product(std::string name, double price, int quantity)
    {
        this->name = name;
        this->price = price;
        this->quantity = quantity;
    }
};

// Function to calculate the total inventory value
double calculateTotalInventoryValue(const std::vector<Product> &products)
{
    double totalValue = 0.0;
    for (const auto &product : products)
    {
        totalValue += product.price * product.quantity;
    }
    return totalValue;
}

// Function to find the most expensive product
std::string findMostExpensiveProduct(const std::vector<Product> &products)
{
    if (products.empty())
        return "";

    const Product *mostExpensive = &products[0];
    for (const auto &product : products)
    {
        if (product.price > mostExpensive->price)
        {
            mostExpensive = &product;
        }
    }
    return mostExpensive->name;
}

// Function to check if a product exists in the inventory
bool isProductInStock(const std::vector<Product> &products, const std::string &productName)
{
    for (const auto &product : products)
    {
        if (product.name == productName)
        {
            return true;
        }
    }
    return false;
}

// Function to sort products
void sortProducts(std::vector<Product> &products, const std::string &criteria, bool ascending)
{
    std::sort(products.begin(), products.end(), [&](const Product &a, const Product &b)
              {
        // Sort by price
        if (criteria == "price") {
            return ascending ? a.price < b.price : a.price > b.price;
        // Sort by quantity
        } else if (criteria == "quantity") {
            return ascending ? a.quantity < b.quantity : a.quantity > b.quantity;
        }
        // Exception
        return false; });
}

// Function to load products from a file
std::vector<Product> loadProductsFromFile(const std::string &filename)
{
    std::vector<Product> products;
    std::ifstream file(filename);

    if (!file.is_open())
    {
        std::cerr << "Failed to open file: " << filename << std::endl;
        return products;
    }

    std::string line;
    while (std::getline(file, line))
    {
        // Skip empty lines and lines containing "Product List"
        if (line.empty() || line.find("Product List") != std::string::npos)
        {
            continue;
        }

        std::istringstream stream(line);
        std::string name, temp;
        double price;
        int quantity;

        std::getline(stream, name, ':'); // Get the product name

        stream >> temp; // skip the "price" label
        stream >> price;
        stream >> temp; // skip ","
        stream >> temp; // skip the "quantity" label
        stream >> quantity;

        products.push_back(Product(name, price, quantity));
    }

    file.close();
    return products;
}

int main()
{
    // Load inventory from file
    std::string filename = "Product List.txt";
    std::vector<Product> inventory = loadProductsFromFile(filename);

    if (inventory.empty())
    {
        std::cerr << "No products loaded from file." << std::endl;
        return 1;
    }

    // Calculate total inventory value
    double totalValue = calculateTotalInventoryValue(inventory);
    std::cout << "Total inventory value: " << totalValue << "\n\n";

    // Find the most expensive product
    std::string mostExpensive = findMostExpensiveProduct(inventory);
    std::cout << "Most expensive product: " << mostExpensive << "\n\n";

    // Check if "Headphones" is in stock
    bool headphonesInStock = isProductInStock(inventory, "Headphones");
    std::cout << "Is 'Headphones' in stock: " << (headphonesInStock ? "true" : "false") << "\n\n";

    // Sort products by price in descending order
    sortProducts(inventory, "price", false);
    std::cout << "Products sorted by price (descending):\n";
    for (const auto &product : inventory)
    {
        std::cout << product.name << ": price " << product.price << ", quantity " << product.quantity << std::endl;
    }

    // Sort products by quantity in ascending order
    sortProducts(inventory, "quantity", true);
    std::cout << "\nProducts sorted by quantity (ascending):\n";
    for (const auto &product : inventory)
    {
        std::cout << product.name << ": price " << product.price << ", quantity " << product.quantity << std::endl;
    }

    return 0;
}