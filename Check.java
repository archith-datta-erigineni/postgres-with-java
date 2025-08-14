package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Check {
    String make;
    String model;
    String year;
    String serial;


    public static void main(String[] args) {
        Check test = new Check("Gonda", "Accord", "2015");
        test.verifyModelYear();
        test.verifyMake();
    }
        


    @SuppressWarnings("deprecation")
    Check(String make, String model, String year) {
        this.make=make;
        this.model=model;
        this.year=year;
    }
    Check(String make, String model, String year, String serial) {
        this.make=make;
        this.model=model;
        this.year=year;
        this.serial=serial;
    }

    public boolean verifyModelYear() {
        try {
            String apiUrl = String.format(
                "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformakeyear/make/%s/modelyear/%s?format=json",
                make, year
            );
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonParser().parse(response.toString()).getAsJsonObject();
                com.google.gson.JsonArray results = jsonObject.getAsJsonArray("Results");
                boolean found = false;
                for (int i = 0; i < results.size(); i++) {
                    com.google.gson.JsonObject obj = results.get(i).getAsJsonObject();
                    String modelName = obj.get("Model_Name").getAsString();
                    if (modelName.equalsIgnoreCase(model)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    System.out.println("Valid combination: " + make + " " + model + " " + year);
                    return true;
                } else {
                    System.out.println("Invalid combination: " + make + " " + model + " " + year);
                    return false;
                }
            } else {
                System.out.println("API request failed.");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean verifyMake() {
        try {
            String apiUrl = "https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                com.google.gson.JsonObject jsonObject = new com.google.gson.JsonParser().parse(response.toString()).getAsJsonObject();
                com.google.gson.JsonArray results = jsonObject.getAsJsonArray("Results");
                boolean found = false;
                for (int i = 0; i < results.size(); i++) {
                    com.google.gson.JsonObject obj = results.get(i).getAsJsonObject();
                    String makelName = obj.get("Make_Name").getAsString();
                    if (makelName.equalsIgnoreCase(make)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    System.out.println("Valid make: " + make);
                    return true;
                } else {
                    System.out.println("Invalid make: " + make);
                    return false;
                }
            } else {
                System.out.println("API request failed.");
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkSerial() {
        try {
            String url = "jdbc:postgresql://localhost:5432/testdb";
            String user = "postgres";
            String password = "test";
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to server");
            Statement stmt = conn.createStatement();

            int rs = stmt.executeUpdate(String.format("select count(*) from inventory where serial='%s';", serial));

            System.out.println("Counted " + rs + " rows");

            stmt.close();
            conn.close();

            if (rs > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

