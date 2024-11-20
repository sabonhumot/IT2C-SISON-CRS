package it2c.sison.crps;

import java.util.Scanner;

public class IT2CSISONCRS {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String response;
        boolean validChoice = false;
        int choice = 0;

        do {

            System.out.println("-------------------------------------");
            System.out.println("|        CONDO RENTAL SYSTEM        |");
            System.out.println("-------------------------------------");
            System.out.println("1. TENANTS");
            System.out.println("2. UNITS");
            System.out.println("3. RENT UNITS");
            System.out.println("4. REPORTS");
            System.out.println("5. EXIT");

            while (!validChoice) {
                System.out.print("Enter action: ");
                String action = input.next().trim();

                try {
                    choice = Integer.parseInt(action);

                    if (choice >= 1 && choice <= 5) {
                        validChoice = true;
                    } else {
                        System.out.print("Invalid option. Please choose between 1 and 5.\n");
                    }

                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please enter a valid number between 1 and 5.\n");
                }
            }

            switch (choice) {

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
            }
            System.out.print("Do you want to continue to Main Menu? (yes/no): ");
            response = input.next();

        } while (response.equalsIgnoreCase("yes"));
        System.out.println("Thank you. Goodbye!");
    }

}
