package org.example.DBA1;

import java.util.Scanner;

public class Login implements ILogin {

    /**
     * @param userName: userName typed data contains username, password, question and answer.
     * @param password: password typed data contains password.
     * @return boolean : it will return boolean values true or false.
     * */
    @Override
    public boolean authenticate(String userName, String password) {
        if(userName == null || password == null) return false;
        if(userName.isEmpty() || password.isEmpty()) return false;
        userName = userName.trim();
        password = password.trim();
        User user = new User();
        boolean status = user.loadUserbyUsername(userName);
        if(status == false) return false;
        try {
            String encryptedPassword = Utils.encryptUsingMD5(password);

            if(!encryptedPassword.equals(user.getPassword()))
                return false;

            Scanner scanner = new Scanner(System.in);

            System.out.println(user.getQuestion());
            String answer = scanner.nextLine();

            if(!answer.equals(user.getAnswer())){
                System.out.println("Your Answer is not correct");
                return false;
            }

        }
        catch (Exception e) {
            System.out.println("Something went wrong!");
            return false;
        }
        System.out.println("You are Successfully Logged in Now");
        return true;
    }
}
