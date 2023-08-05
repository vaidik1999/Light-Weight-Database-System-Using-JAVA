package org.example.DBA1;

import java.util.Scanner;

public class Main {

    public static boolean register(Scanner input){
        String username;
        String password;
        String question;
        String answer;
        System.out.println("Enter your username");
        username = input.nextLine();
        System.out.println("Enter your password");
        password = input.nextLine();
        System.out.println("Enter your question");
        question = input.nextLine();
        System.out.println("Enter your answer");
        answer = input.nextLine();
        User user = new User(username,password,question,answer);
        Register obj= new Register();

        int status = obj.registerUser(user);

        if(status == 0){
            System.out.println("You are successfully registered, you can log in now");
        }
        else if(status == 1){
            System.out.println("You have already registered");
        }
        else{
            System.out.println("Exception occured during registration");
        }
        return status == 0? true : false;
    }

    public static boolean login(Scanner input){
        String username;
        String password;
        System.out.println("Enter your username");
        username = input.nextLine();
        System.out.println("Enter your password");
        password = input.nextLine();
        ILogin obj = new Login();
        boolean status = obj.authenticate(username,password);
        if(status == true){
//            System.out.println("User logged in successfully");
        }
        return status;
    }
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        int state;
        System.out.println("Enter your choice: \n 1. For login \n 2. For register");
        state = Integer.parseInt(input.nextLine());

        boolean status = true;

        switch (state){
            case 2: status = register(input);
            if(!status) break;
            case 1: status = login(input);
            break;
            default: break;
        }

        if(status){

            while(true){
                System.out.println("Enter your query");
                String query = input.nextLine();

                IQuery q = new Query();

                q.runQuery(query);

            }
        }
    }
}
