package it2c.sison.crps;

import java.util.Scanner;

public class Tenants {

    private void addTenants() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter First Name: ");
        String fname = input.next();
        while (fname.isEmpty() || !fname.matches("[a-zA-Z]+")) {
            System.out.print("First Name cannot be empty and must only contain letters. Please enter again: ");
            fname = input.next();
        }

        System.out.print("Enter Last Name: ");
        String lname = input.next();
        while (lname.isEmpty() || !lname.matches("[a-zA-Z]+")) {
            System.out.print("Last Name cannot be empty and must only contain letters. Please enter again: ");
            lname = input.next();
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = input.next();

            if (isValidEmail(email)) {
                break;
            } else {
                System.out.print("Invalid email format. Please try again.\n");
            }
        }

        String cntctno;
        while (true) {
            System.out.print("Enter Contact Number: ");
            cntctno = input.next();

            if (isValidPNum(cntctno)) {
                break;
            } else {
                System.out.print("Invalid phone number. Please try again.\n");
            }
        }

//        System.out.print("Enter Status (Active/Inactive): ");
//        String status = input.next();
//        while (!status.equalsIgnoreCase("Active") && !status.equalsIgnoreCase("Inactive")) {
//            System.out.print("Invalid status. Please enter either 'Active' or 'Inactive': ");
//            status = input.nextLine();
//        }

        String status = "Inactive";

        String sql = "INSERT INTO tenants (fname, lname, email, contact, t_status) VALUES (?, ?, ?, ?, ?)";
        conf.addTenants(sql, fname, lname, email, cntctno, status);
        
        
        

    }

    public void viewTenants() {
        String tqry = "SELECT * FROM tenants";
        String[] hrds = {"ID", "First Name", "Last Name", "Email", "Contact No.", "Status"};
        String[] clmns = {"id", "fname", "lname", "email", "contact", "t_status"};

        config conf = new config();

        conf.viewRecords(tqry, hrds, clmns);
    }

    private void updateTenant() {
        Scanner input = new Scanner(System.in);
        config conf = new config();
        int id = 0;
        boolean validId = false;

        while (!validId) {
            System.out.print("Enter Tenant ID to Update: ");
            String uInput = input.next().trim();

            try {
                id = Integer.parseInt(uInput);

                if (id > 0) {
                    validId = true;
                } else {
                    System.out.println("Tenant ID must be a positive number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        String sql = "SELECT id FROM tenants WHERE id = ?";
        while (conf.getSingleValue(sql, id) == 0) {
            System.out.print("Tenant does not exist. Please try again: ");
            id = input.nextInt();
        }

        System.out.print("Enter new Email: ");
        String uemail = input.next();

        System.out.print("Enter new Contact Number: ");
        String ucontact = input.next();

        System.out.print("Enter new Tenant Status: ");
        String status = input.next();

        String qry = "UPDATE tenants SET email = ?, contact = ?, t_status = ? WHERE id = ?";

        conf = new config();
        conf.updateRecord(qry, uemail, ucontact, status, id);
    }

    private void deleteTenant() {
        Scanner input = new Scanner(System.in);
        int id = 0;
        boolean validId = false;

        config conf = new config();

        while (!validId) {
            System.out.print("Enter Tenant ID to Delete: ");
            String uInput = input.next().trim();

            try {
                id = Integer.parseInt(uInput);

                if (id > 0) {
                    validId = true;
                } else {
                    System.out.println("Tenant ID must be a positive number. Please try again.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        String sql = "SELECT id FROM tenants WHERE id = ?";
        while (conf.getSingleValue(sql, id) == 0) {
            System.out.print("Tenant does not exist. Please try again: ");
            id = input.nextInt();
        }

        while (conf.tStatus(id).equalsIgnoreCase("Active")) {
            System.out.print("You cannot delete an Active Tenant. Please Try again: ");
            id = input.nextInt();

            while (conf.getSingleValue(sql, id) == 0) {
                System.out.print("Tenant does not exist. Please try again: ");
                id = input.nextInt();
            }

        }

        String sqlDelete = "DELETE FROM tenants WHERE id = ?";

        conf.deleteRecord(sqlDelete, id);

    }

    public void TenantsOp() {
        Tenants tnts = new Tenants();
        Scanner input = new Scanner(System.in);
        String response = "";

        int choice = 0;

        do {
            boolean validChoice = false;

            System.out.println("-------------------------------------");
            System.out.println("|            TENANT PANEL           |");
            System.out.println("-------------------------------------");
            System.out.println("1. ADD TENANT");
            System.out.println("2. UPDATE TENANT");
            System.out.println("3. VIEW TENANTS");
            System.out.println("4. DELETE TENANT");
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
                    tnts.addTenants();
                    break;

                case 2:
                    tnts.viewTenants();
                    tnts.updateTenant();
                    tnts.viewTenants();
                    break;

                case 3:
                    tnts.viewTenants();
                    break;

                case 4:
                    tnts.viewTenants();
                    tnts.deleteTenant();
                    tnts.viewTenants();
                    break;

                case 5:
                    System.out.println("\nReturning to Main Menu...");
                    return;

            }

            boolean validResponse = false;
            input.nextLine();
            while (!validResponse) {

                System.out.print("Do you want to continue to Tenants Menu? (yes/no): ");
                response = input.nextLine().trim();

                if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {

                    validResponse = true;

                } else if (response.isEmpty()) {
                    System.out.println("Input cannot be empty. Please try again.");
                } else {
                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                }
            }

        } while (response.equalsIgnoreCase("yes"));

    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPNum(String phoneNumber) {
        String phoneRegex = "^[0-9]{11}$";
        return phoneNumber.matches(phoneRegex);
    }

}
