package com.mcgill.ecse428.textbook_exchange.cucumber.steps;

import io.cucumber.java.en.Given;

public class SimpleSteps {

    @Given("I print a message")
    public void i_print_a_message() {
        System.out.println("Cucumber is running!");
    }
}