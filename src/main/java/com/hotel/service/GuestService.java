package com.hotel.service;

import com.hotel.model.Guest;
import com.hotel.repository.impl.GuestRepository;

public class GuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest saveOrUpdate(Guest guest) {
        return guestRepository.findByPassport(guest.getPassportNumber())
                .map(existing -> {
                    existing.setFirstName(guest.getFirstName());
                    existing.setLastName(guest.getLastName());
                    existing.setPhone(guest.getPhone());
                    existing.setEmail(guest.getEmail());
                    existing.setCountry(guest.getCountry());
                    existing.setAddress(guest.getAddress());
                    existing.setCity(guest.getCity());
                    existing.setEmergencyContact(guest.getEmergencyContact());
                    existing.setEmergencyPhone(guest.getEmergencyPhone());
                    if (guest.isLoyaltyMember()) {
                        existing.setLoyaltyMember(true);
                        if (existing.getLoyaltyNumber() == null || existing.getLoyaltyNumber().isBlank()) {
                            existing.setLoyaltyNumber(guest.getLoyaltyNumber());
                        }
                    }
                    return guestRepository.save(existing);
                })
                .orElseGet(() -> guestRepository.save(guest));
    }
}
