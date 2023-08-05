package org.example.DBA1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User implements IUser{
    private String userName;

    private String password;

    private String question;

    private String answer;

    public User(){

    }
    public User(String userName, String password, String question, String answer){
        this.userName= userName;
        this.password=password;
        this.question=question;
        this.answer=answer;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    //Reference : https://www.digitalocean.com/community/tutorials/java-append-to-file
    public static List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try {
            File file = new File("db/users.txt");
            file.createNewFile();
            BufferedReader allUsersFile = new BufferedReader(new FileReader(file));
            List<String> allUsersList = Utils.getFileContentList(allUsersFile);
            for(String user: allUsersList) {
                List<String> userInfo = List.of(user.split("-"));
                allUsers.add(new User(userInfo.get(0), userInfo.get(1), userInfo.get(2), userInfo.get(3)));
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return allUsers;
    }


    /**
     * @param userName: userName typed data contains username, password, question and answer.
     * @return static : it will return static values.
     * */
    @Override
    public boolean loadUserbyUsername(String userName) {
        List<User> allUsers = this.getAllUsers();
        if(allUsers == null) return false;
        for(User user: allUsers) {
            if(user.getUserName().equals(userName)){
                this.userName = user.getUserName();
                this.password = user.getPassword();
                this.question = user.getQuestion();
                this.answer = user.getAnswer();
                return true;
            }
        }
        return false;
    }
}
