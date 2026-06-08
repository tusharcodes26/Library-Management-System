package service;

import dao.AdminDAO;

public class AdminService {

    private AdminDAO dao =
            new AdminDAO();

    public boolean login(String username,
                         String password) {

        if(username.trim().isEmpty()) {

            return false;
        }

        if(password.trim().isEmpty()) {

            return false;
        }

        return dao.login(username,password);
    }
}