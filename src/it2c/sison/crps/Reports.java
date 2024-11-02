package it2c.sison.crps;

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

            switch (action) {
                case 1:

                    break;

                case 2:
                    
                    Reports rprts = new Reports();
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

        System.out.println("----------------------------------------------------");
        System.out.println("|             GENERAL REPORT OF TENANTS            |");
        System.out.println("----------------------------------------------------");
        
        
        
        
        
        
        
        
        System.out.println("\n----------------------------------------------------");
        System.out.println("|             SUMMARY OF AVAILABLE UNITS           |");
        System.out.println("----------------------------------------------------");
        
        

    }

    private void IndividualReport() {
        Scanner input = new Scanner(System.in);
        Tenants tnts = new Tenants();
        
        tnts.viewTenants();
        
        System.out.print("Enter Tenant ID: ");
        int tid = input.nextInt();
        
        conf.generateIndividualReport(tid);
        
    }

}
