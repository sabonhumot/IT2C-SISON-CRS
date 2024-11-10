package it2c.sison.crps;

import java.util.Scanner;

public class Tenants {

    private void addTenants() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter First Name: ");
        String fname = input.next();

        System.out.print("Enter Last Name: ");
        String lname = input.next();

        System.out.print("Enter eMail: ");
        String email = input.next();

        System.out.print("Enter Contact Number: ");
        String cntctno = input.next();
        
        System.out.print("Enter Status: ");
        String status = input.next();

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

        String sql = "SELECT id FROM tenants WHERE id = ?";

        System.out.print("Enter Tenant ID: ");
        int id = input.nextInt();
        while (conf.getSingleValue(sql, id) == 0) {

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

        System.out.print("Enter ID to Delete: ");
        int id = input.nextInt();

        config conf = new config();

        String sqlDelete = "DELETE FROM tenants WHERE id = ?";
//        int studentIdToDelete = 1;

        conf.deleteRecord(sqlDelete, id);

    }

    public void TenantsOp() {
        Tenants tnts = new Tenants();
        Scanner input = new Scanner(System.in);
        String response;

        do {

            System.out.println("-------------------------------------");
            System.out.println("|            TENANT PANEL           |");
            System.out.println("-------------------------------------");
            System.out.println("1. ADD TENANT");
            System.out.println("2. UPDATE TENANT");
            System.out.println("3. VIEW TENANTS");
            System.out.println("4. DELETE TENANT");
            System.out.println("5. EXIT TO MAIN MENU");

            System.out.print("Enter action: ");
            int action = input.nextInt();

            switch (action) {
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

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            System.out.print("Continue to Tenants Panel? (yes/no): ");
            response = input.next();

        } while (response.equalsIgnoreCase("yes"));

    }

}
