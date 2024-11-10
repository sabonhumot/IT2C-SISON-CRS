package it2c.sison.crps;

import java.util.Scanner;

public class IT2CSISONCRS {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String response;

        do {

            System.out.println("-------------------------------------");
            System.out.println("|        CONDO RENTAL SYSTEM        |");
            System.out.println("-------------------------------------");
            System.out.println("1. TENANTS");
            System.out.println("2. UNITS");
            System.out.println("3. RENT UNITS");
            System.out.println("4. REPORTS");
            System.out.println("5. EXIT");

            System.out.print("Enter action: ");
            int action = input.nextInt();

            switch (action) {

                case 1:
                    Tenants tnts = new Tenants();

                    tnts.TenantsOp();

                    break;

                case 2:
                    Units units = new Units();

                    units.UnitsOp();

                    break;

                case 3:
                    Rental rent = new Rental();

                    rent.rentUnit();

                    break;

                case 4:
                    
                    Reports rprts = new Reports();
                    
                    rprts.ReportsOp();
                    
                    
                    
                    break;

                case 5:
                    System.out.println("Exiting the program. Goodbye !");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            System.out.print("Do you want to continue? (yes/no): ");
            response = input.next();

        } while (response.equalsIgnoreCase("yes"));
        System.out.println("Thank you. Goodbye!");
    }

}
