package org.example.DBA1;

import java.io.*;

public class Register implements IRegister {

    /**
     * @param user: User typed data contains username, password, question and answer.
     * @return int: it will return int values.
     * */
    @Override
    public int registerUser(User user) {
        User retrievedUser = new User();
        try {
            boolean status = retrievedUser.loadUserbyUsername(user.getUserName());
            if(status) {
                return 1;
            }

            File file = new File("db/users.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter allUsersFile = new PrintWriter(br);
            String userString = user.getUserName() + "-" + Utils.encryptUsingMD5(user.getPassword()) + "-" + user.getQuestion() + "-" + user.getAnswer();
            allUsersFile.println(userString);
            allUsersFile.close();
        } catch (Exception e) {
            System.out.println(e);
            return 2;
        }
        return 0;
    }

}
