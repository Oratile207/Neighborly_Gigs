package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.User;

public interface AuthService {
    User registerUser(String firstName, String lastName, String email, String password, String phone);    //User registerUser(String name, String email, String password, String phone);

}
