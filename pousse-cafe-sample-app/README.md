# Pousse-Café Sample App

This project is the Sample App module of Pousse-Café. It provides a concrete example of how the Sample Meta App can be 
integrated in an actual back-end application. This example features a Spring Boot application exposing a REST API
whose resources interact with the Meta-Application.

# Test the app

1. Build and launch the app
2. Create a product with the following command:

    curl -X POST "http://localhost:8080/product" -d '{"key":"id"}' -H "Content-Type: application/json"

3. Fetch the product:

    curl -X GET "http://localhost:8080/product/id"
