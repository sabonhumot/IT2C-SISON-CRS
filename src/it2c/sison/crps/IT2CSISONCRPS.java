package it2c.sison.crps;

import java.util.Scanner;

public class IT2CSISONCRPS {

    public void addTenants () {
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

            switch (action) {

                case 1:
                    IT2CSISONCRPS demo = new IT2CSISONCRPS();
                    demo.addTenants();
                break;

            }

            System.out.print("Continue? (yes/no): ");
            response = input.next();

        } while (response.equals("yes"));
        System.out.println("Thank you. See you again.");

    }

}
