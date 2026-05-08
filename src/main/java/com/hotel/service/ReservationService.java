package com.hotel.service;

import com.hotel.events.AdminAvailabilityObserver;
import com.hotel.events.AvailabilityNotifier;
import com.hotel.model.Guest;
import com.hotel.model.Reservation;
import com.hotel.model.enums.ReservationStatus;
import com.hotel.model.enums.Role;
import com.hotel.model.enums.RoomType;
import com.hotel.repository.impl.ReservationRepository;
import com.hotel.service.strategy.DiscountBillingStrategy;
import com.hotel.util.IdGenerator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private static final double TAX_RATE = 0.13;
    private final ReservationRepository reservationRepository;
    private final GuestService guestService;
    private final RoomService roomService;
    private final LoyaltyService loyaltyService;
    private final ActivityLogService activityLogService;
    private final AvailabilityNotifier availabilityNotifier;

    public ReservationService(ReservationRepository reservationRepository,
                              GuestService guestService,
                              RoomService roomService,
                              LoyaltyService loyaltyService,
                              ActivityLogService activityLogService,
                              AvailabilityNotifier availabilityNotifier) {
        this.reservationRepository = reservationRepository;
        this.guestService = guestService;
        this.roomService = roomService;
        this.loyaltyService = loyaltyService;
        this.activityLogService = activityLogService;
        this.availabilityNotifier = availabilityNotifier;
        this.availabilityNotifier.subscribe(new AdminAvailabilityObserver());
    }

    public Reservation createReservation(Guest guest,
                                         RoomType roomType,
                                         int roomCount,
                                         int adults,
                                         int children,
                                         LocalDate checkIn,
                                         LocalDate checkOut,
                                         String addOnSummary,
                                         String specialRequests,
                                         double roomCharge,
                                         double addonsCharge,
                                         boolean loyaltyEnrollment) {
        validateOccupancy(roomType, roomCount, adults, children);

        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Invalid stay dates.");
        }

        if (loyaltyEnrollment) {
            guest.setLoyaltyMember(true);
            loyaltyService.ensureLoyaltyEnrollment(guest);
        }
        Guest savedGuest = guestService.saveOrUpdate(guest);

        Reservation reservation = new Reservation();
        reservation.setReservationCode(IdGenerator.reservationCode());
        reservation.setGuest(savedGuest);
        reservation.setRoomType(roomType);
        reservation.setRoomCount(roomCount);
        reservation.setAdults(adults);
        reservation.setChildren(children);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setAddOnSummary(addOnSummary);
        reservation.setSpecialRequests(specialRequests);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setRoomCharge(roomCharge);
        reservation.setAddonsCharge(addonsCharge);
        double taxable = roomCharge + addonsCharge;
        reservation.setTaxAmount(round(taxable * TAX_RATE));
        reservation.setTotalAmount(round(taxable + reservation.getTaxAmount()));
        reservation.setOutstandingBalance(reservation.getTotalAmount());

        Reservation saved = reservationRepository.save(reservation);
        activityLogService.log(savedGuest.getFullName(), "CREATE_RESERVATION", "Reservation", saved.getReservationCode(), "Reservation created successfully.");
        return saved;
    }

    public Reservation findByCode(String code) {
        return reservationRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));
    }

    public List<Reservation> search(String keyword) {
        return reservationRepository.search(keyword == null ? "" : keyword);
    }

    public Reservation applyDiscount(String code, double percent, Role role, String actor) {
        double cap = role == Role.MANAGER ? 30.0 : 15.0;
        if (percent < 0 || percent > cap) {
            throw new IllegalArgumentException("Discount exceeds the allowed cap for this role.");
        }
        Reservation reservation = findByCode(code);
        double beforeTax = reservation.getRoomCharge() + reservation.getAddonsCharge();
        double afterDiscount = new DiscountBillingStrategy(percent).apply(beforeTax);
        reservation.setDiscountAmount(round(beforeTax - afterDiscount));
        reservation.setTaxAmount(round((afterDiscount) * TAX_RATE));
        reservation.setTotalAmount(round(afterDiscount + reservation.getTaxAmount()));
        reservation.setOutstandingBalance(round(Math.max(0, reservation.getTotalAmount() - reservation.getDepositPaid())));
        Reservation saved = reservationRepository.save(reservation);
        activityLogService.log(actor, "APPLY_DISCOUNT", "Reservation", code, "Applied discount: " + percent + "%.");
        return saved;
    }

    public Reservation checkout(String code, String actor) {
        Reservation reservation = findByCode(code);
        if (reservation.getOutstandingBalance() > 0) {
            throw new IllegalArgumentException("Cannot checkout while balance remains outstanding.");
        }
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        Reservation saved = reservationRepository.save(reservation);
        activityLogService.log(actor, "CHECKOUT", "Reservation", code, "Guest checked out successfully.");
        availabilityNotifier.notifyAll(saved.getRoomType(), "Rooms became available after checkout for reservation " + code);
        return saved;
    }

    public Reservation updateAfterPayment(Reservation reservation, double newOutstanding, double paidAmount) {
        reservation.setDepositPaid(round(reservation.getDepositPaid() + paidAmount));
        reservation.setOutstandingBalance(round(newOutstanding));
        return reservationRepository.save(reservation);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public long activeReservationCount(LocalDate start, LocalDate end) {
        return reservationRepository.countActiveReservationsByDateRange(start, end);
    }

    public double applyWeekendMultiplier(double basePrice, LocalDate start, LocalDate end) {
        double total = 0;
        LocalDate date = start;
        while (date.isBefore(end)) {
            double daily = basePrice;
            if (date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                daily *= 1.20;
            }
            total += daily;
            date = date.plusDays(1);
        }
        return round(total);
    }

    public void validateOccupancy(RoomType roomType, int roomCount, int adults, int children) {
        int totalGuests = adults + children;
        int totalCapacity = roomType.getCapacity() * roomCount;
        if (totalGuests <= 0) {
            throw new IllegalArgumentException("At least one guest is required.");
        }
        if (totalGuests > totalCapacity) {
            throw new IllegalArgumentException("Selected room plan exceeds occupancy limits.");
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
