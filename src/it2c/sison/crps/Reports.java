package it2c.sison.crps;

import java.time.LocalDate;
import java.util.Scanner;

public class Reports {

    config conf = new config();

    public void ReportsOp() {
        Scanner input = new Scanner(System.in);
        String response;

        do {
            System.out.println("-------------------------------------");
            System.out.println("|           REPORTS PANEL           |");
            System.out.println("-------------------------------------");
            System.out.println("1. GENERAL REPORT");
            System.out.println("2. INDIVIDUAL REPORT");
            System.out.println("3. EXIT TO MAIN MENU");

            System.out.print("Enter action: ");
            int action = input.nextInt();
            
            Reports rprts = new Reports();
            
            switch (action) {
                case 1:
                    
                   rprts.GeneralReport();

                    break;

                case 2:

                    
                    rprts.IndividualReport();

                    break;

                case 3:
                    System.out.println("Returning to Main Menu...\n");
                    return;
                default:
                    System.out.println("Invalid Option. Please try again.");
                    break;
            }
            System.out.print("Continue to Main Menu? (yes/no): ");
            response = input.next();

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

        String sql1 = "SELECT t_status FROM tenants WHERE id = ?";
        String tStatus = conf.getSingleValue1(sql, tid);

        if ("Inactive".equalsIgnoreCase(tStatus)) {
            System.out.println("Tenant is not currently renting a unit.");
            return;
        }

//        while ("Inactive".equalsIgnoreCase(tStatus)) {
//            System.out.print("Tenant is not currently renting a unit. Please try again: ");
//            tid = input.nextInt();
//        }
        conf.generateIndividualReport(tid);

    }

}
