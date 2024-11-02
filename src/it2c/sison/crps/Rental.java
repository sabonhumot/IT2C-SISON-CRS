package it2c.sison.crps;

import java.time.LocalDate;
import java.util.Scanner;

public class Rental {

    config conf = new config();
    int tid;
    LocalDate leaseStart, leaseEnd;

    public void rentUnit() {
        Scanner input = new Scanner(System.in);

        Tenants tnts = new Tenants();
        Rental rent = new Rental();

        System.out.println("-------------------------------------");
        System.out.println("|             RENT UNIT             |");
        System.out.println("-------------------------------------");
        tnts.viewTenants();

        System.out.print("Enter Tenant ID: ");
        tid = input.nextInt();

        String sql1 = "SELECT id FROM tenants WHERE id = ?";
        while (conf.getSingleValue(sql1, tid) == 0) {
            System.out.print("Tenant does not exist. Please try again: ");
            tid = input.nextInt();
        }

        rent.viewAvailableUnits();

        rent.rentalProcess();

    }

    private void viewAvailableUnits() {

        String tqry = "SELECT * FROM units WHERE u_status = 'Available'";
        String[] hrds = {"Unit Number", "Unit Type", "Monthly Rental", "Status"};
        String[] clmns = {"unit_id", "unit_type", "monthly_rental", "u_status"};

        conf.viewRecords(tqry, hrds, clmns);
    }

    private void rentalProcess() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter Unit Number you want to rent: ");
        int unum = input.nextInt();

        conf.fetchUnitDetails(unum);

        conf.reservationConfirmation(unum);
        conf.leaseAgreement(unum);
        
        conf.generateLeaseDates(unum);

        System.out.print("\nWould you like to proceed to payment? (yes/no): ");
        String response = input.next();

        if (response.equalsIgnoreCase("yes")) {
            String sql2 = "UPDATE units SET u_status = 'Occupied' WHERE unit_id = ?";
            String sql3 = "UPDATE units SET lease_start ?, lease_end = ? WHERE unit_id = ?";
            
            conf.updateRecord(sql2, unum);
            conf.updateRecord(sql3, unum);

            conf.payment(unum);
        } else {
            System.out.println("You chose not to rent this unit.");
        }

    }
    
    private void leaseDates (int leaseDurationMonths) {
        
        leaseStart = LocalDate.now();
        leaseEnd = leaseStart.plusMonths(leaseDurationMonths);
    }

}
