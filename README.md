# Exchange Application

The Exchange application provides services for creating users and currency exchange. Below, you'll find details on prerequisites, installation, usage, and licensing.

## Prerequisites
Make sure you have the following installed:
- Java 17
- Maven 3.8.1

## Installation
To set up the application, follow these steps:

1. **Clone the repository:**
    ```sh
    git clone https://github.com/piotrg44/exchange.git
    cd exchange
    ```

2. **Build the project:**
    ```sh
    mvn clean install
    ```

3. **Run the application:**
    ```sh
    # You need to put correct credentials to your database.
    # Alternatively, you can use environment variables.
    mvn clean install -Dexec.args="-Dspring.datasource.username= -Dspring.datasource.password= -Dspring.datasource.url="
    ```

## Usage
Access the application's controllers by visiting the following link in your browser:
http://localhost:8080/swagger-ui/index.html

## License
This project is licensed under the MIT License.