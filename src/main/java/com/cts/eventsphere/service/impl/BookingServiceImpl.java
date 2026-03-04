package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.mapper.booking.BookingRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.booking.BookingResponseDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.model.Booking;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.repository.BookingRepository;
import com.cts.eventsphere.repository.VenueRepository;
import com.cts.eventsphere.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for Booking operations
 * * @author 2479476
 * @version 1.0
 * @since 04-03-2026
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final VenueRepository venueRepository;
    private final VenueRequestDtoMapper venueRequestDtoMapper;
    private final VenueResponseDtoMapper venueResponseDtoMapper;

    private final BookingRepository bookingRepository;
    private final BookingRequestDtoMapper requestMapper;
    private final BookingResponseDtoMapper responseMapper;

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        // Convert DTO to Entity
        Booking booking = requestMapper.toEntity(bookingRequestDto);

        // Ensure initial status is 'pending' as per workflow requirements
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.pending);
        }

        // Save to Database
        Booking savedBooking = bookingRepository.save(booking);

        // Return mapped Response DTO
        return responseMapper.toDto(savedBooking);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public BookingResponseDto updateBookingStatus(String bookingId, BookingStatus newStatus) {

        // 2. Fetch the existing booking
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Update failed: Booking ID " + bookingId + " does not exist."));

        if (newStatus == BookingStatus.confirmed) {

            Venue venue = venueRepository.findByVenueId(existingBooking.getVenueId());



            venue.setAvailabilityStatus(AvailabilityStatus.unavailable);
            venueRepository.save(venue);
        }

        // 4. Update and save the booking
        existingBooking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(existingBooking);

        return responseMapper.toDto(updatedBooking);
    }

    @Override
    public void deleteBooking(String bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new RuntimeException("Cannot delete: Booking not found with ID: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }
}