package it2c.sison.crps;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;

public class config {
    //Connection Method to SQLITE

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:crps.db"); // Establish connection
            System.out.println("");
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
        Scanner input = new Scanner(System.in);
        config conf = new config();

        String sql = "UPDATE units SET u_status = 'Reserved' WHERE unit_id = ?";

        System.out.println("-------------------------------------");
        System.out.println("|      RESERVATION CONFIRMATION     |");
        System.out.println("-------------------------------------");

        System.out.print("\n Would you like to reserve this unit? (yes/ no): ");
        String choice = input.next();

        if (choice.equalsIgnoreCase("yes")) {
            System.out.printf("You have successfully reserved Unit %s.\n", unitId);
            conf.updateRecord(sql, unitId);
            System.out.println("Please review the Lease Agreement.");
        } else {
            System.out.println("You chose not to reserve this unit.");
            System.exit(0);
        }

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

                double monthlyRental = rs.getDouble("monthly_rental");
                System.out.println("-------------------------------------");
                System.out.println("|          PAYMENT PROCESS          |");
                System.out.println("-------------------------------------");

                System.out.printf("Amount needed to pay: P%s\n", monthlyRental);

                System.out.print("Enter amount to pay: ");
                int pay = input.nextInt();
                double change = 0.0;

                while (pay < monthlyRental) {
                    System.out.print("Payment insufficient. Please try again: ");
                    pay = input.nextInt();

                    if (pay > monthlyRental) {
                        change = (double) pay - monthlyRental;

                        System.out.printf("\nChange = P%.2f\n", change);
                    }
                }

                System.out.printf("You have successfully rented Unit No. %s\n", unitId);

            }

        } catch (SQLException e) {
            System.out.println("Error fetching unit details for payment: " + e.getMessage());
        }
    }

    public void generateIndividualReport(int tenantId) {

        config conf = new config();

        System.out.println("\n-------------------------------------");
        System.out.println("|      INDIVIDUAL RENTAL REPORT      |");
        System.out.println("-------------------------------------");

        String query = "SELECT t.id AS id, t.fname AS fname, t.lname AS lname, "
                + "t.email, t.contact, u.unit_id, u.unit_type, u.monthly_rental, u.u_status, "
                + "r.lease_start, r.lease_end, u.amenities, r.rental_id "
                + "FROM tenants t "
                + "JOIN rentals r ON t.id = r.tenant_id "
                + "JOIN units u ON r.unit_id = u.unit_id "
                + "WHERE t.id = ?";

        try (Connection conn = config.connectDB()) {

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, tenantId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        int id = rs.getInt("id");
                        int rid = rs.getInt("rental_id");
                        String firstName = rs.getString("fname");
                        String lastName = rs.getString("lname");
                        String email = rs.getString("email");
                        String contact = rs.getString("contact");
                        int unitId = rs.getInt("unit_id");
                        String unitType = rs.getString("unit_type");
                        double monthlyRental = rs.getDouble("monthly_rental");
                        String unitStatus = rs.getString("u_status");
                        String amenities = rs.getString("amenities");
                        String leaseStart = rs.getString("lease_start");
                        String leaseEnd = rs.getString("lease_end");

                        System.out.printf("Rental ID: %d", rid);

                        System.out.println("\n--------------------------------------------------------------");

                        System.out.printf("\n -Tenant ID: %d", id);
                        System.out.printf("\n -First Name: %s", firstName);
                        System.out.printf("\n -Last Name: %s", lastName);
                        System.out.printf("\n -Email: %s", email);
                        System.out.printf("\n -Contact: %s", contact);
                        System.out.println("\n--------------------------------------------------------------");
                        System.out.printf("\n -Unit ID: %d", unitId);
                        System.out.printf("\n -Unit Type: %s", unitType);
                        System.out.printf("\n -Monthly Rental: %.2f", monthlyRental);
                        System.out.printf("\n -Amenities: %s", amenities);
                        conf.generateLeaseDates(unitId);
                        System.out.printf("\n-Unit Status: %s", unitStatus);

                        System.out.println("\n--------------------------------------------------------------");

                        LocalDate repGen = LocalDate.now();
                        System.out.printf("REPORT GENERATED ON %s\n", repGen);

                    } else {
                        System.out.println("Tenant not found.");
                        return;
                    }
                }
            }

            System.out.println("--------------------------------------------------------------------------\n");
        } catch (SQLException e) {
            System.out.println("Error generating individual report: " + e.getMessage());
        }
    }

    public void generateLeaseDates(int unitId) {

        String sql = "SELECT lease_terms FROM units WHERE unit_id = ?";

        try (Connection conn = connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, unitId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    long leaseTerms = rs.getLong("lease_terms");

                    LocalDate leaseStart = LocalDate.now();
                    System.out.printf("- Lease Start: %s", leaseStart);

                    LocalDate leaseEnd = leaseStart.plusMonths(rs.getLong("lease_terms"));
                    System.out.printf("\n- Lease End: %s", leaseEnd);

                } else {
                    System.out.printf("No unit found with ID: %s", unitId);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching unit details: " + e.getMessage());

        }

    }

    public void addRental(int tenantId, int unitId) {

        String sql = "INSERT INTO rentals (tenant_id, unit_id, lease_start, lease_end) VALUES (?, ?, ?, ?)";

        config conf = new config();

        LocalDate leaseStart = LocalDate.now();
        LocalDate leaseEnd = leaseStart.plusYears(1);

        try (Connection conn = connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tenantId);
            pstmt.setInt(2, unitId);
            pstmt.setDate(3, Date.valueOf(leaseStart));
            pstmt.setDate(4, Date.valueOf(leaseEnd));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("");
            } else {
                System.out.println("Failed to add rental record.");
            }

        } catch (SQLException e) {
        }

    }

    public static boolean isTenantEligible(int tenantId) {
        String sql = "SELECT t_status FROM tenants WHERE id = ?";
        try (Connection conn = config.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tenantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getString("t_status").equalsIgnoreCase("Active")) {
                    return true; // Tenant is eligible
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking tenant eligibility: " + e.getMessage());
        }
        return false;
    }

    public boolean isUnitAvailable(int unitId) {
        String sql = "SELECT u_status FROM units WHERE unit_id = ?";
        try (Connection conn = config.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, unitId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getString("u_status").equalsIgnoreCase("Available")) {
                    return true; // Unit is available
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking unit availability: " + e.getMessage());
        }
        return false;
    }

    public static String tStatus(int tenantId) {
        String sql = "SELECT t_status FROM tenants WHERE id = ?";
        try (Connection conn = config.connectDB(); 
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tenantId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("t_status");
            } else {
                return null; 
            }

        } catch (SQLException e) {
            System.out.println("Error fetching tenant status: " + e.getMessage());
            return null;
        }

    }

    public static String uStatus(int unitId) {
        String sql = "SELECT u_status FROM units WHERE unit_id = ?";
        try (Connection conn = config.connectDB();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, unitId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("u_status");
            } else {
                return null; 
            }
        } catch (SQLException e) {
            System.out.println("Error checking unit availability: " + e.getMessage());
            return null;
        }     
    }

}
