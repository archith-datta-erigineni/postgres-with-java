# postgres-with-java
This is a simple Java Swing App demonstrating how to query Postgres using Java.


The app supports adding, retrieving, deleting, and editing cars in an inventory table in Postgres. Cars are validating for legitimacy using the NHTSA Vehicle API.

Features
  Add Cars — Insert new cars into database
  Retrieve Inventory — View all previously stored cars
  Edit Cars — Update make, model, year, color, or VIN by ID
  Delete Cars — Remove cars from the database by ID
  VIN Duplication Check — Prevents adding cars with the same VIN
  Validation — Uses the NHTSA API to confirm the car exists
  
Tech Stack
  Java 17+ (Swing for GUI)
  PostgreSQL (JDBC for database connectivity)
  Gson (for JSON parsing from API)
  NHTSA Vehicle API (external validation)

I also created a Medium article going through setting up Postgres and connecting it to the IDE:  
