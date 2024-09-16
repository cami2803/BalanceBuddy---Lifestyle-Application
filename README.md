# BalanceBuddy - Lifestyle Application
BalanceBuddy is a mobile application designed to help users improve their lifestyle by monitoring their daily habits.


# Setup

This section provides a comprehensive guide on setting up the BalanceBuddy project environment, including installation instructions, configuration steps, and necessary setup for databases and other components.

## Prerequisites

Before proceeding with the setup, ensure you have the following installed and configured on your system:

- **Java 17**: Install from [oracle.com](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Node.js and npm**: Install Node.js from [nodejs.org](https://nodejs.org/en), npm will be installed alongside Node.js.
- **MySQL**: Install from [downloads.mysql](https://downloads.mysql.com/archives/community/)

## Setup Steps

After installing all the required technologies, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/cami2803/BalanceBuddy-Lifestyle-Application.git
    cd BalanceBuddy-Lifestyle-Application
    ```

2. Ensure Java 17 is correctly installed and configured in your PATH environment variable. You can refer to Oracle's documentation [here](https://docs.oracle.com/cd/F74770_01/English/Installing/p6_eppm_install_config/89522.htm).

## Configuration

1. Navigate to the backend directory:
    ```bash
    cd backend
    ```

2. Open the `application.properties` file (located in `src/main/resources`) and configure the MySQL database connection:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/balancebuddy-lifestyle-application
    spring.datasource.username=username
    spring.datasource.password=password
    ```
    Replace `username` and `password` with your MySQL credentials.

## Running the Application

1. Open a terminal or command prompt and navigate to the backend directory of the cloned repository.
    ```bash
    cd backend
    mvn spring-boot:run
    ```

2. Open another terminal or command prompt window and navigate to the frontend directory of the cloned repository.
    ```bash
    cd frontend
    npm install
    npm start
    ```
