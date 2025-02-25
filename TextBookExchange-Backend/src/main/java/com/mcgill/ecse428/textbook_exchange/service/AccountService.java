package com.mcgill.ecse428.textbook_exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Account;
import com.mcgill.ecse428.textbook_exchange.model.Admin;
import com.mcgill.ecse428.textbook_exchange.model.Cart;
import com.mcgill.ecse428.textbook_exchange.model.User;
import com.mcgill.ecse428.textbook_exchange.repository.AccountRepository;
import com.mcgill.ecse428.textbook_exchange.repository.AdminRepository;
import com.mcgill.ecse428.textbook_exchange.repository.CartRepository;
import com.mcgill.ecse428.textbook_exchange.repository.UserRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    public Admin createAdmin(String email, String username, String password, String phoneNumber) {
        if (email == null || email.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Email cannot be empty");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password cannot be empty");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Phone number cannot be empty");
        }
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
        Admin admin = new Admin(email, username, password, phoneNumber);
        adminRepository.save(admin);
        return admin;
    }

    public Admin getAdmin(String email) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Admin not found");
        }
        return admin;
    }

    public Iterable<Account> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin updateAdmin(String email, String username, String password, String phoneNumber) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Admin not found");
        }
        if (username != null && !username.trim().isEmpty()) {
            admin.setUsername(username);
        }
        if (password != null && !password.trim().isEmpty()) {
            admin.setPassword(password);
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            admin.setPhoneNumber(phoneNumber);
        }
        adminRepository.save(admin);
        return admin;
    }

    public void deleteAdmin(String email) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Admin not found");
        }
        adminRepository.delete(admin);
    }
    public void deleteAllAdmins() {
        adminRepository.deleteAll();
    }

    public User createUser(String email, String username, String password, String phoneNumber) {
        if (email == null || email.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Email cannot be empty");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password must be at least 8 characters long");
        }
        // Check if the password is only numbers
        if (password.replaceAll("[^0-9]", "").length() == password.length()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password must contain at least one letter");
        }
        if (password.toLowerCase().equals(password)){
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password must contain at least one uppercase letter");
        }
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Phone number cannot be empty");
        }
        if (phoneNumber.replaceAll("[^0-9]", "").length() != phoneNumber.length()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Phone number must contain only numbers");
        }
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Email already exists");
        }
        Cart cart = new Cart();
        cartRepository.save(cart);
        


        User user = new User(email, username, password, phoneNumber,cart );
        userRepository.save(user);
        return user;
    }

    public User getUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"User not found");
        }
        return user;
    }
    public Iterable<Account> getAllUsers() {
        return userRepository.findAll();
    }
    public User updateUser(String email, String username, String password, String phoneNumber) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"User not found");
        }
        if (username != null && !username.trim().isEmpty()) {
            user.setUsername(username);
        }
        if (password != null && !password.trim().isEmpty()) {
            if (password.length() < 8) {
                throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password must be at least 8 characters long");
            }
            // Check if the password is only numbers
            if (password.replaceAll("[^0-9]", "").length() == password.length()) {
                throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password must contain at least one letter");
            }
            if (password.toLowerCase().equals(password)){
                throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Password must contain at least one uppercase letter");
            }
            if (user.getPassword().equals(password)) {
                throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"New password cannot be the same as the old password");
            }
            user.setPassword(password);
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            if (!(phoneNumber.replaceAll("[^0-9]", "").length() != phoneNumber.length())) {
                user.setPhoneNumber(phoneNumber);   
            }
        }
        userRepository.save(user);
        return user;
    }
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"User not found");
        }
        userRepository.delete(user);
    }
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
    




    
}
