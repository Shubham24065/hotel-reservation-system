package com.hotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class BookingSession {
    private static final BookingSession INSTANCE = new BookingSession();

    private int adults;
    private int children;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String passportNumber;
    private String country;
    private String address;
    private String city;
    private String emergencyContact;
    private String emergencyPhone;
    private String specialRequests;
    private boolean loyaltyEnrollment;
    private String selectedRoomType;
    private int roomCount = 1;
    private double selectedRoomPrice;
    private double addonsPrice;
    private final List<String> addOns = new ArrayList<>();

    private BookingSession() {}

    public static BookingSession getInstance() { return INSTANCE; }

    public void reset() {
        adults = 0;
        children = 0;
        checkInDate = null;
        checkOutDate = null;
        firstName = null;
        lastName = null;
        phone = null;
        email = null;
        passportNumber = null;
        country = null;
        address = null;
        city = null;
        emergencyContact = null;
        emergencyPhone = null;
        specialRequests = null;
        loyaltyEnrollment = false;
        selectedRoomType = null;
        roomCount = 1;
        selectedRoomPrice = 0;
        addonsPrice = 0;
        addOns.clear();
    }

    public int getAdults() { return adults; }
    public void setAdults(int adults) { this.adults = adults; }
    public int getChildren() { return children; }
    public void setChildren(int children) { this.children = children; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public boolean isLoyaltyEnrollment() { return loyaltyEnrollment; }
    public void setLoyaltyEnrollment(boolean loyaltyEnrollment) { this.loyaltyEnrollment = loyaltyEnrollment; }
    public String getSelectedRoomType() { return selectedRoomType; }
    public void setSelectedRoomType(String selectedRoomType) { this.selectedRoomType = selectedRoomType; }
    public int getRoomCount() { return roomCount; }
    public void setRoomCount(int roomCount) { this.roomCount = roomCount; }
    public List<String> getAddons() { return addOns; }
    public double getSelectedRoomPrice() { return selectedRoomPrice; }
    public void setSelectedRoomPrice(double selectedRoomPrice) { this.selectedRoomPrice = selectedRoomPrice; }
    public double getAddonsPrice() { return addonsPrice; }
    public void setAddonsPrice(double addonsPrice) { this.addonsPrice = addonsPrice; }
}
