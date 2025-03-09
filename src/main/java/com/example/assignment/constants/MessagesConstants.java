package com.example.assignment.constants;

public class MessagesConstants {
  public static final String STATUS_200 = "200";
  public static final String MESSAGE_200 = "Request processed successfully";
  public static final String STATUS_201 = "201";
  public static final String MESSAGE_201 = "User created successfully";
  public static final String STATUS_401 = "401";
  public static final String MESSAGE_401 = "Unauthorized";
  public static final String STATUS_409 = "409";
  public static final String MESSAGE_409 = "Already registered";
  public static final String STATUS_417 = "417";
  public static final String MESSAGE_417_UPDATE =
      "Update operation failed. Please try again or contact Dev team";
  public static final String MESSAGE_417_DELETE =
      "Delete operation failed. Please try again or contact Dev team";
  public static final String STATUS_500 = "500";
  public static final String MESSAGE_500 = "HTTP Status Internal Server Error";

  private MessagesConstants() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
