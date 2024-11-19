package it2c.sison.crps;

import java.util.Scanner;

public class Units {

    private void addUnit() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Unit Type: ");
        String utype = input.nextLine();
        while (utype.isEmpty() || !utype.matches("[a-zA-Z\\s]+")) {
            System.out.print("Invalid Unit Type. Please enter a valid type: ");
            utype = input.nextLine();
        }

        System.out.print("Enter Unit Footage (sqm): ");
        String ufootage = input.nextLine();

        System.out.print("Enter Unit Floor Number: ");
        String ufloornum = input.nextLine();

        System.out.print("Enter Monthly Rental: ");
        String monthly = input.nextLine();

        System.out.print("Amenities Included: ");
        String amenities = input.nextLine();

        System.out.print("Lease Terms: ");
        String leaseT = input.nextLine();

        System.out.print("Enter Unit Status (Available/Occupied): ");
        String ustatus = input.next();
        while (!ustatus.equalsIgnoreCase("Available") && !ustatus.equalsIgnoreCase("Occupied")) {
            System.out.println("Invalid status. Please enter either 'Available' or 'Occupied'.");
            ustatus = input.nextLine();
        }

        String sql = "INSERT INTO units (unit_type, sqm, floor_num, monthly_rental, amenities, lease_terms, u_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        conf.addTenants(sql, utype, ufootage, ufloornum, monthly, amenities, leaseT, ustatus);

    }

    public void viewUnits() {
        String tqry = "SELECT * FROM units";
        String[] hrds = {"Unit Number", "Unit Type", "Unit Footage", "Unit Floor Number", "Amenities", "Monthly Rental", "Lease Terms", "Status"};
        String[] clmns = {"unit_id", "unit_type", "sqm", "floor_num", "amenities", "monthly_rental", "lease_terms", "u_status"};

        config conf = new config();

        conf.viewRecords(tqry, hrds, clmns);
    }

    private void updateUnit() {
        Scanner input = new Scanner(System.in);
        config conf = new config();
        String sql = "SELECT unit_id FROM units WHERE unit_id = ?";

        System.out.print("Enter Unit ID: ");
        int id = input.nextInt();
        while (conf.getSingleValue(sql, id) == 0) {
        }

        System.out.print("Enter new Monthly Rental: ");
        String monthly = input.next();

        System.out.print("Enter new Amenities Included: ");
        String amenities = input.next();

        System.out.print("Enter new Unit Status: ");
        String status = input.next();

        String qry = "UPDATE units SET monthly_rental = ?, amenities = ?, u_status = ? WHERE unit_id = ?";

        conf = new config();
        conf.updateRecord(qry, monthly, amenities, status, id);
    }

    private void deleteUnit() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter ID to Delete: ");
        int id = input.nextInt();

        config conf = new config();

        String sqlDelete = "DELETE FROM units WHERE unit_id = ?";
//        int studentIdToDelete = 1;

        conf.deleteRecord(sqlDelete, id);

    }

    public void UnitsOp() {
        Units units = new Units();
        Scanner input = new Scanner(System.in);
        String response;
        boolean validChoice = false;
        int choice = 0;

        do {

            System.out.println("-------------------------------------");
            System.out.println("|             UNIT PANEL            |");
            System.out.println("-------------------------------------");
            System.out.println("1. ADD UNIT");
            System.out.println("2. UPDATE UNIT");
            System.out.println("3. VIEW UNITS");
            System.out.println("4. DELETE UNIT");
            System.out.println("5. EXIT TO MAIN MENU");

            while (!validChoice) {
                System.out.print("Enter action: ");
                String action = input.next().trim();

                try {
                    choice = Integer.parseInt(action);

                    if (choice >= 1 && choice <= 5) {
                        validChoice = true;
                    } else {
                        System.out.print("Invalid option. Please choose between 1 and 5: ");
                    }

                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please enter a valid number between 1 and 5: ");
                }
            }

            switch (choice) {
                case 1:
                    units.addUnit();
                    break;

                case 2:
                    units.viewUnits();
                    units.updateUnit();
                    units.viewUnits();
                    break;

                case 3:
                    units.viewUnits();
                    break;

                case 4:
                    units.viewUnits();
                    units.deleteUnit();
                    units.viewUnits();
                    break;

                case 5:
                    System.out.println("\nReturning to Main Menu...\n");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            System.out.print("Continue to Units Panel? (yes/no): ");
            response = input.next();

        } while (response.equalsIgnoreCase("yes"));

    }

}
