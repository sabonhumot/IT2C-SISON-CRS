package it2c.sison.crps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class config {
    //Connection Method to SQLITE

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:crps.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    public void addTenants(String sql, String... values) {
        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    // Dynamic view method to display records from any table
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        // Check that columnHeaders and columnNames arrays are the same length
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
                ResultSet rs = pstmt.executeQuery()) {

            // Print the headers dynamically
            StringBuilder headerLine = new StringBuilder();
            headerLine.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-25s | ", header)); // Adjust formatting as needed
            }
            headerLine.append("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            System.out.println(headerLine.toString());

            // Print the rows dynamically based on the provided column names
            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-25s | ", value != null ? value : "")); // Adjust formatting
                }
                System.out.println(row.toString());
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // UPDATE METHOD
    //-----------------------------------------------
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB(); // Use the connectDB method
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    // Add this method in the config class
    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // Helper Method for Setting PreparedStatement Values
    //-----------------------------------------------
    private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]);
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]);
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]);
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]);
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]);
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]);
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
            } else {
                pstmt.setString(i + 1, values[i].toString());
            }
        }
    }

    //-----------------------------------------------
    // GET SINGLE VALUE METHOD
    //-----------------------------------------------
    public double getSingleValue(String sql, Object... params) {
        double result = 0.0;
        try (Connection conn = connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, params);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving single value: " + e.getMessage());
        }
        return result;
    }

    public void fetchUnitDetails(int unitId) {
        String sql = "SELECT * FROM units WHERE unit_id = ?";

        try (Connection conn = connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, unitId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    String unitType = rs.getString("unit_type");
                    String floor = rs.getString("floor_num");
                    String size = rs.getString("sqm");
                    String monthlyRent = rs.getString("monthly_rental");
                    String status = rs.getString("u_status");
                    String amenities = rs.getString("amenities");
                    String leaseTerms = rs.getString("lease_terms");

                    System.out.println("-------------------------------------");
                    System.out.println("|           UNIT DETAILS             |");
                    System.out.println("-------------------------------------");
                    System.out.printf("\nUnit ID: %s", unitId);
                    System.out.printf("\nType: %s", unitType);
                    System.out.printf("\nFloor: %s", floor);
                    System.out.printf("\nSize: %s", size);
                    System.out.printf("\nMonthly Rent: P%s", monthlyRent);
                    System.out.printf("\nAmenities: %s", amenities);
                    System.out.printf("\nLease Terms: %s\n", leaseTerms);
                    System.out.printf("\nStatus: %s\n", status);
                } else {
                    System.out.printf("No unit found with ID: %s", unitId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching unit details: " + e.getMessage());
        }
    }

    public void reservationConfirmation(int unitId) {
        System.out.println("-------------------------------------");
        System.out.println("|      RESERVATION CONFIRMATION     |");
        System.out.println("-------------------------------------");

        System.out.printf("You have successfully reserved Unit %s.\n", unitId);
        System.out.println("Please review the Lease Agreement.");

    }

    public void leaseAgreement(int unitId) {
        String sql = "SELECT monthly_rental FROM units WHERE unit_id = ?";

        try (Connection conn = connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, unitId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    String monthlyRent = rs.getString("monthly_rental");

                    System.out.println("-------------------------------------");
                    System.out.println("|          LEASE AGREEMENT          |");
                    System.out.println("-------------------------------------");

                    System.out.printf("- Monthly Rent: P%s\n", monthlyRent);
                    System.out.println("- Payment Due Date: Every 1st of the Month.");
                    System.out.println("- Minimum Lease Duration: 6 months");
                    System.out.printf("\n- Downpayment Required: P%s\n", monthlyRent);
                } else {
                    System.out.printf("\nNo unit found with ID: %s", unitId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching unit details: " + e.getMessage());
        }
    }

    public void payment(int unitId) {
        Scanner input = new Scanner(System.in);
        String sql = "SELECT monthly_rental FROM units WHERE unit_id = ?";

        try (Connection conn = connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, unitId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {

                String monthlyRental = rs.getString("monthly_rental");
                System.out.println("-------------------------------------");
                System.out.println("|          PAYMENT PROCESS          |");
                System.out.println("-------------------------------------");

                System.out.printf("Amount needed to pay: %s\n", monthlyRental);

                System.out.print("Enter amount to pay: ");
                int pay = input.nextInt();

                System.out.printf("You have successfully rented Unit No. %s\n", unitId);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching unit details for payment: " + e.getMessage());
        }
    }
    
    public void generateIndividualReport(int tenantId) {
        String tenantQuery = "SELECT * FROM tenants WHERE id = ?";
        String unitQuery = "SELECT * FROM units WHERE tenant_id = ?";
        String paymentQuery = "SELECT payment_date, amount_paid, payment_status FROM payments WHERE tenant_id = ?";

        try (Connection conn = config.connectDB()) {
            // Fetch Tenant Info
            try (PreparedStatement pstmt = conn.prepareStatement(tenantQuery)) {
                pstmt.setInt(1, tenantId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("\n-------------------------------------");
                        System.out.println("|      INDIVIDUAL RENTAL REPORT      |");
                        System.out.println("-------------------------------------");
                        System.out.printf("Tenant ID: %d\n", tenantId);
                        System.out.printf("Name: %s %s\n", rs.getString("fname"), rs.getString("lname"));
                        System.out.printf("Contact: %s\n", rs.getString("email"));
                    } else {
                        System.out.println("Tenant not found.");
                        return;
                    }
                }
            }

            // Fetch Unit Info
            try (PreparedStatement pstmt = conn.prepareStatement(unitQuery)) {
                pstmt.setInt(1, tenantId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.printf("Unit Number: %s | Type: %s | Monthly Rent: %s\n",
                                rs.getString("unit_id"), rs.getString("unit_type"), rs.getString("monthly_rental"));
                        System.out.printf("Lease Start Date: %s | Lease End Date: %s\n",
                                rs.getString("lease_start"), rs.getString("lease_end"));
                        System.out.printf("Status: %s\n", rs.getString("u_status"));
                    }
                }
            }

            // Fetch Payment History
            System.out.println("\n--- Payment History ---");
            try (PreparedStatement pstmt = conn.prepareStatement(paymentQuery)) {
                pstmt.setInt(1, tenantId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("Payment Date: %s | Amount Paid: %s | Status: %s\n",
                                rs.getString("payment_date"), rs.getDouble("amount_paid"), rs.getString("payment_status"));
                    }
                }
            }
            System.out.println("-------------------------------------\n");
        } catch (SQLException e) {
            System.out.println("Error generating individual report: " + e.getMessage());
        }
    }

}
