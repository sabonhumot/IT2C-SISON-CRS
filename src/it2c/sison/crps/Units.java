package it2c.sison.crps;

import java.util.Scanner;

public class Units {

    private void addUnit() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter Unit Type: ");
        String utype = input.nextLine();

        String ufootage;
        while (true) {

            System.out.print("Enter Unit Footage (sqm): ");
            ufootage = input.nextLine().trim();

            try {

                if (Double.parseDouble(ufootage) > 0) {
                    break;
                } else {
                    System.out.println("Size must be a positive number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number value.");
            }

        }

        String ufloornum;
        while (true) {

            System.out.print("Enter Unit Floor Number: ");
            ufloornum = input.nextLine().trim();

            try {

                if (Integer.parseInt(ufloornum) > 0) {
                    break;
                } else {
                    System.out.println("Floor Number must be a positive number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number value.");
            }
        }

        String monthly;
        while (true) {

            System.out.print("Enter Monthly Rental (Include centavos if applicable): ");
            monthly = input.nextLine();

            try {
                if (Double.parseDouble(monthly) > 0) {
                    break;
                } else {
                    System.out.println("Monthly Rental must be a positive number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number value.");
            }
        }

        System.out.print("Amenities Included: ");
        String amenities = input.nextLine();

        String leaseT;

        while (true) {

            System.out.print("Lease Terms (1 - 12): ");
            leaseT = input.nextLine().trim();

            if (leaseT.matches("[1-9]|1[0-2]")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 12.");

            }
        }

//        System.out.print("Enter Unit Status (Available/Occupied): ");
//        String ustatus = input.next().trim();
//        while (!ustatus.equalsIgnoreCase("Available") && !ustatus.equalsIgnoreCase("Occupied")) {
//            System.out.println("Invalid status. Please enter either 'Available' or 'Occupied'.");
//            ustatus = input.nextLine();
//        }
        String ustatus = "Available";

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
        int id = 0;
        boolean validId = false;

        while (!validId) {
            System.out.print("Enter Unit ID: ");
            String uInput = input.next().trim();

            try {

                id = Integer.parseInt(uInput);

                if (id > 0) {
                    validId = true;
                } else {
                    System.out.println("Unit ID must be a positive number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        String sql = "SELECT unit_id FROM units WHERE unit_id = ?";
        while (conf.getSingleValue(sql, id) == 0) {
            System.out.println("Unit does not exist. Please try again.");
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
        config conf = new config();
        int id = 0;
        boolean validId = false;

        while (!validId) {

            System.out.print("Enter Unit ID to Delete: ");
            String uInput = input.next().trim();

            try {
                id = Integer.parseInt(uInput);

                if (id > 0) {
                    validId = true;
                } else {
                    System.out.println("Unit ID must be a number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }

        }

        while (conf.tStatus(id).equalsIgnoreCase("Occupied")) {
            System.out.print("You cannot delete an Occupied Unit. Please try again: ");
            id = input.nextInt();

        }

        String sqlDelete = "DELETE FROM units WHERE unit_id = ?";

        conf.deleteRecord(sqlDelete, id);

    }

    public void UnitsOp() {
        Units units = new Units();
        Scanner input = new Scanner(System.in);
        String response = "";

        int choice = 0;

        do {
            boolean validChoice = false;

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

            }

            input.nextLine();

            boolean validResponse = false;

            while (!validResponse) {
                System.out.print("Do you want to continue to Units Menu? (yes/no): ");
                response = input.next();

                if (response.isEmpty()) {
                    System.out.print("Input cannot be empty. Please input 'yes' or 'no'.");
                } else if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please input 'yes' or 'no'.");
                }
            }
        } while (response.equalsIgnoreCase("yes"));

    }

}
