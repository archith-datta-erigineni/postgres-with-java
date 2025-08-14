# postgres-with-java
This is a simple Java Swing App demonstrating how to query Postgres using Java.


The app supports adding, retrieving, deleting, and editing cars in an inventory table in Postgres. Cars are validated for legitimacy using the NHTSA Vehicle API.

**Features**

  + Add Cars — Insert new cars into database
  
  + Retrieve Inventory — View all previously stored cars
  
  + Edit Cars — Update make, model, year, color, or VIN by ID
  
  + Delete Cars — Remove cars from the database by ID
  
  + VIN Duplication Check — Prevents adding cars with the same VIN
  
  + Validation — Uses the NHTSA API to confirm the car exists
  
  
**Tech Stack**

  - Java 17+ (Swing for GUI)
  
  - PostgreSQL (JDBC for database connectivity)
  
  - Gson (for JSON parsing from API)
  
  - NHTSA Vehicle API (external validation)
  

I also created a Medium article going through setting up Postgres and connecting it to the IDE:  

https://medium.com/@architherigineni/connecting-postgresql-to-visual-studio-code-using-java-93be66242a0a


**Flowchart**

    src/
    
     └── com/example/
     
         ├── CarApp.java   # Main GUI class
         
         └── Check.java    # Validation helper class (API + VIN checks)

**Potential Improvements**

While this project works well for demonstrating using Java to query Postgres, the code can definitely be tweaked:

  ~ Use PreparedStatement: Using Statement encourages String concatenation, which is prone to SQL injection
  
  ~ Pool Connections: Repeatedly opening and closing connections degrades efficency and performance
  
  ~ Error Handling: Replace printStackTrace() with more user-friendly error messages
  
  ~ Separate UI and DB Code: Move DB logic to a separate class or method to avoid confusion and redundancy
  
