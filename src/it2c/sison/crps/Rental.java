package it2c.sison.crps;

import java.util.Scanner;

public class Rental {

    config conf = new config();
    int tid;

    public void rentUnit() {
        Scanner input = new Scanner(System.in);

        Tenants tnts = new Tenants();
        Rental rent = new Rental();

        System.out.println("-------------------------------------");
        System.out.println("|             RENT UNIT             |");
        System.out.println("-------------------------------------");
        rent.viewInactiveTenants();

        System.out.print("Enter Tenant ID: ");
        tid = input.nextInt();

        String sql = "SELECT id FROM tenants WHERE id = ?";
        while (conf.getSingleValue(sql, tid) == 0) {
            System.out.print("Tenant does not exist. Please try again: ");
            tid = input.nextInt();
        }

        while (config.isTenantEligible(tid)) {

            System.out.print("Tenant is already renting a unit. Please try again: ");
            tid = input.nextInt();

            while (conf.getSingleValue(sql, tid) == 0) {
                System.out.print("Tenant does not exist. Please try again: ");
                tid = input.nextInt();
            }

        }

        rent.viewAvailableUnits();

        System.out.print("Enter Unit Number you want to rent: ");
        int unum = input.nextInt();

        String sql2 = "SELECT unit_id FROM units WHERE unit_id = ?";
        while (conf.getSingleValue(sql2, unum) == 0) {
            System.out.print("Unit does not exist. Please try again: ");
            unum = input.nextInt();
        }

        while (conf.isUnitAvailable(tid)) {

            System.out.print("Unit is already occupied. Please try again: ");
            unum = input.nextInt();

            while (conf.getSingleValue(sql2, unum) == 0) {
                System.out.print("Unit does not exist. Please try again: ");
                unum = input.nextInt();
            }
        }

        conf.fetchUnitDetails(unum);

        conf.reservationConfirmation(unum);

        conf.leaseAgreement(unum);

        conf.generateLeaseDates(unum);

        System.out.print("\nWould you like to proceed to payment? (yes/no): ");
        String response = input.next();

        if (response.equalsIgnoreCase("yes")) {

            String sql4 = "UPDATE units SET u_status = 'Occupied' WHERE unit_id = ?";
            String sql6 = "UPDATE tenants SET t_status = 'Active' WHERE id = ?";

            conf.updateRecord(sql4, unum);
            conf.addRental(tid, unum);
            conf.updateRecord(sql6, tid);

            conf.payment(unum);
        } else {
            System.out.println("You chose not to rent this unit.");
        }

    }

    private void viewAvailableUnits() {

        String uqry = "SELECT * FROM units WHERE u_status = 'Available'";
        String[] hrds = {"Unit Number", "Unit Type", "Monthly Rental", "Status"};
        String[] clmns = {"unit_id", "unit_type", "monthly_rental", "u_status"};

        conf.viewRecords(uqry, hrds, clmns);
    }

    public void viewInactiveTenants() {

        String tqry = "SELECT * FROM tenants WHERE t_status = 'Inactive'";
        String[] hrds = {"ID", "First Name", "Last Name", "Email", "Contact No.", "Status"};
        String[] clmns = {"id", "fname", "lname", "email", "contact", "t_status"};

        conf.viewRecords(tqry, hrds, clmns);
    }
}
