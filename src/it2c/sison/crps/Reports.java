package it2c.sison.crps;

import java.time.LocalDate;
import java.util.Scanner;

public class Reports {

    config conf = new config();

    public void ReportsOp() {
        Scanner input = new Scanner(System.in);
        String response = "";
        boolean validChoice = false;
        int choice = 0;

        do {
            System.out.println("-------------------------------------");
            System.out.println("|           REPORTS PANEL           |");
            System.out.println("-------------------------------------");
            System.out.println("1. GENERAL REPORT");
            System.out.println("2. INDIVIDUAL REPORT");
            System.out.println("3. EXIT TO MAIN MENU");

            while (!validChoice) {
                System.out.print("Enter action: ");
                String action = input.next().trim();

                try {
                    choice = Integer.parseInt(action);

                    if (choice >= 1 && choice <= 3) {
                        validChoice = true;
                    } else {
                        System.out.print("Invalid option. Please choose between 1 and 3.\n ");
                    }

                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please enter a valid number between 1 and 3.\n ");
                }
            }

            Reports rprts = new Reports();

            switch (choice) {
                case 1:

                    rprts.GeneralReport();

                    break;

                case 2:

                    rprts.IndividualReport();

                    break;

                case 3:
                    System.out.println("Returning to Main Menu...\n");
                    return;
            }
            
            input.nextLine();
            
            boolean validResponse = false;
            
            while (!validResponse) {
                System.out.print("Do you want to continue to Main Menu? (yes/no): ");
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

    private void GeneralReport() {

        config conf = new config();

        System.out.println("----------------------------------------------------");
        System.out.println("|             GENERAL REPORT OF TENANTS            |");
        System.out.println("----------------------------------------------------");

        String tQry = "SELECT * FROM tenants WHERE t_status = 'Active'";
        String[] hdrs = {"ID", "First Name", "Last Name", "Email", "Contact No."};
        String[] clmns = {"id", "fname", "lname", "email", "contact"};

        conf.viewRecords(tQry, hdrs, clmns);

        System.out.println("\n----------------------------------------------------");
        System.out.println("|             SUMMARY OF Rented UNITS              |");
        System.out.println("----------------------------------------------------");

        String uQry = "SELECT * FROM units WHERE u_status = 'Occupied'";
        String[] hdrs1 = {"Unit Number", "Unit Type", "Monthly Rental", "Status"};
        String[] clmns1 = {"unit_id", "unit_type", "monthly_rental", "u_status"};

        LocalDate repGen = LocalDate.now();

        conf.viewRecords(uQry, hdrs1, clmns1);

        System.out.println("\n----------------------------------------------------\n");
        System.out.printf("|             REPORT GENERATED ON %s              |", repGen);
        System.out.println("----------------------------------------------------");

    }

    private void IndividualReport() {
        Scanner input = new Scanner(System.in);
        Tenants tnts = new Tenants();

        tnts.viewTenants();

        System.out.print("Enter Tenant ID: ");
        int tid = input.nextInt();

        String sql = "SELECT id FROM tenants WHERE id = ?";
        while (conf.getSingleValue(sql, tid) == 0) {
            System.out.print("Tenant does not exist. Please try again: ");
            tid = input.nextInt();
        }

        while (config.isTenantEligible(tid)) {

            System.out.print("Tenant is not currently renting a unit. Please try again: ");
            tid = input.nextInt();

            while (conf.getSingleValue(sql, tid) == 0) {
                System.out.print("Tenant does not exist. Please try again: ");
                tid = input.nextInt();
            }

        }

        conf.generateIndividualReport(tid);

    }

}
