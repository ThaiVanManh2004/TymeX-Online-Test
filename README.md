# TymeX-Online-Test

## Challenge 1 - Basic Mobile Application Development

This simple **Currency Converter** application is a user-friendly tool that allows users to convert currency values between different currencies.

This application is developed using **Kotlin** and **Jetpack Compose**. This app uses the **ExchangeRatesAPI** from https://exchangeratesapi.io to fetch real-time and accurate exchange rates.

You can build and run the application using **Android Studio**. The app is designed to be simple and easy to use. The user can select the base currency and the target currency from the dropdown menus. The user can then enter the amount in the base currency, and the app will automatically convert the amount to the target currency.

Video demonstrating the app's key features: https://youtu.be/G04XRzFWEkU

## Challenge 2 - The coding skills assessment

### Question 2.1. Product Inventory Management

This solution solves a product inventory management problem with the following tasks:

1. **Calculate Total Inventory Value**: Compute the total value of all products in stock by multiplying their price by quantity.
2. **Find the Most Expensive Product**: Identify the product with the highest price.
3. **Check Product Availability**: Determine if a product with a specific name (e.g., "Headphones") exists in the inventory.
4. **Sort Products**: Sort the inventory by price or quantity in ascending or descending order.

The solution is implemented in C++.

### Question 2.2. Array Manipulation and Missing Number Problem

#### Problem Description

You have an array of n distinct numbers ranging from 1 to n + 1. One number from this range is missing. Write a function to find and return the missing number. The array will always contain distinct numbers.

#### Example

```
Input: [3, 7, 1, 2, 8, 4, 5]
Output: 6
```

#### Solution summary

1. Calculate the expected sum of all numbers from 1 to n + 1 using Gauss sum formula.
2. Compute the actual sum of all numbers present in the array.
3. The missing number is the expected sum minus the actual sum.

The solution is implemented in C++.