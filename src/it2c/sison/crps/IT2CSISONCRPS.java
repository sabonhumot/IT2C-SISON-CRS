package it2c.sison.crps;

import java.util.Scanner;

public class IT2CSISONCRPS {

    public void addTenants() {
        Scanner input = new Scanner(System.in);
        config conf = new config();

        System.out.print("Enter ID: ");
        String id = input.next();

        System.out.print("Enter First Name: ");
        String fname = input.next();

        System.out.print("Enter Last Name: ");
        String lname = input.next();

        System.out.print("Enter eMail: ");
        String email = input.next();

        System.out.print("Enter Contact Number: ");
        String cntctno = input.next();

        String sql = "INSERT INTO tenants (id, fname, lname, email, contact) VALUES (?, ?, ?, ?, ?)";
        conf.addTenants(sql, id, fname, lname, email, cntctno);

    }

    private void viewTenants() {
        String tqry = "SELECT * FROM tenants";
        String[] hrds = {"ID", "First Name", "Last Name", "Email", "Contact No."};
        String[] clmns = {"id", "fname", "lname", "email", "contact"};

        config conf = new config();

        conf.viewRecords(tqry, hrds, clmns);
    }

    private void updateTenant() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Tenant ID: ");
        int id = input.nextInt();

        System.out.print("Enter new First Name: ");
        String ufname = input.next();

        System.out.print("Enter new Last Name: ");
        String ulname = input.next();

        System.out.print("Enter new Email: ");
        String uemail = input.next();

        System.out.print("Enter new Contact No.");
        String ucontact = input.next();

        String qry = "UPDATE tenants SET fname = ?, lname = ?, email = ?, contact = ? WHERE id = ?";

        config conf = new config();
        conf.updateRecord(qry, ufname, ulname, uemail, ucontact, id);
    }

    public void deleteTenant() {
        IT2CSISONCRPS crps = new IT2CSISONCRPS();
        crps.viewTenants();
        
        Scanner input = new Scanner(System.in);

        System.out.print("Enter ID to Delete: ");
        int id = input.nextInt();

        config conf = new config();

        String sqlDelete = "DELETE FROM tenants WHERE id = ?";
//        int studentIdToDelete = 1;

        conf.deleteRecord(sqlDelete, id);

    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String response;

        do {

            System.out.println("1. ADD");
            System.out.println("2. VIEW");
            System.out.println("3. UPDATE");
            System.out.println("4. REMOVE");

            System.out.print("Enter action: ");
            int action = input.nextInt();

            IT2CSISONCRPS crps = new IT2CSISONCRPS();
            switch (action) {

                case 1:

                    crps.addTenants();
                    break;

                case 2:

                    crps.viewTenants();

                    break;

                case 3:                  
                    crps.updateTenant();
                    break;

                case 4:
                    crps.deleteTenant();
                    break;
            }

            System.out.print("Continue? (yes/no): ");
            response = input.next();

        } while (response.equals("yes"));
        System.out.println("Thank you. See you again.");

    }

}
