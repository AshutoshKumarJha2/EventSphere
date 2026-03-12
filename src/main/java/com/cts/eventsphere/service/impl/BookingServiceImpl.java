package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.mapper.booking.BookingRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.booking.BookingResponseDtoMapper;
import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.exception.booking.BookingNotFoundException;
import com.cts.eventsphere.exception.venue.VenueNotFoundException;
import com.cts.eventsphere.model.Booking;
import com.cts.eventsphere.model.ResourceAllocation;
import com.cts.eventsphere.model.Venue;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.repository.BookingRepository;
import com.cts.eventsphere.repository.ResourceAllocationRepository;
import com.cts.eventsphere.repository.VenueRepository;
import com.cts.eventsphere.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for Booking operations
 * @author 2479476
 * @version 1.1
 * @since 04-03-2026
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final VenueRepository venueRepository;
    private final ResourceAllocationRepository resourceAllocationRepository;
    private final BookingRepository bookingRepository;
    private final BookingRequestDtoMapper requestMapper;
    private final BookingResponseDtoMapper responseMapper;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        log.info("Attempting to create a new booking for event ID: {}", bookingRequestDto.eventId());

        Booking booking = requestMapper.toEntity(bookingRequestDto);

        Venue venue = venueRepository.findById(bookingRequestDto.venueId())
                .orElseThrow(() -> {
                    log.error("Venue lookup failed for ID: {}", bookingRequestDto.venueId());
                    return new VenueNotFoundException("Venue not found with id: " + bookingRequestDto.venueId());
                });

        booking.setVenue(venue);

        if (booking.getDate() == null) {
            booking.setDate(LocalDate.now());
        }

        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.pending);
        }

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Successfully created booking with ID: {}", savedBooking.getBookingId());

        return responseMapper.toDto(savedBooking, new ArrayList<>());
    }

    @Override
    public List<BookingResponseDto> getAllBookingsServ() {
        log.info("Fetching all bookings from database");
        return bookingRepository.findAll()
                .stream()
                .map(booking -> {
                    List<ResourceListElementDto> resources = resourceAllocationRepository
                            .findByEvent_EventId(booking.getEventId())
                            .stream()
                            .map(allocation -> new ResourceListElementDto(
                                    allocation.getResource().getName(),
                                    allocation.getQuantity()
                            ))
                            .toList();

                    return responseMapper.toDto(booking, resources);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByVenue(String venueId) {
        log.info("Fetching bookings for venue ID: {}", venueId);

        return bookingRepository.findByVenue_VenueId(venueId)
                .stream()
                .map(booking -> {
                    // Fetch resources for the event associated with this booking
                    List<ResourceListElementDto> resources = resourceAllocationRepository
                            .findByEvent_EventId(booking.getEventId())
                            .stream()
                            .map(a -> new ResourceListElementDto(a.getResource().getName(), a.getQuantity()))
                            .toList();

                    return responseMapper.toDto(booking, resources);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByEvent(String eventId) {
        log.info("Fetching bookings for event ID: {}", eventId);

        List<Booking> bookings = bookingRepository.findByEventId(eventId);
        List<ResourceAllocation> allocations = resourceAllocationRepository.findByEvent_EventId(eventId);

        return bookings.stream().map(booking -> {
            List<ResourceListElementDto> resourceList = allocations.stream()
                    .filter(a -> a.getEvent().getVenueId().equals(booking.getVenue().getVenueId()))
                    .map(a -> new ResourceListElementDto(
                            a.getResource().getName(),
                            a.getResource().getUnit()
                    ))
                    .toList();

            return new BookingResponseDto(
                    booking.getBookingId(),
                    booking.getEventId(),
                    booking.getVenue().getVenueId(),
                    booking.getDate(),
                    booking.getStatus(),
                    resourceList,
                    booking.getCreatedAt(),
                    booking.getUpdatedAt()
            );
        }).toList();
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(String bookingId, BookingStatus newStatus) {
        log.info("Updating status for booking ID: {} to {}", bookingId, newStatus);

        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Update failed: Booking ID {} not found", bookingId);
                    return new BookingNotFoundException("Update failed: Booking ID " + bookingId + " does not exist.");
                });

        if (newStatus == BookingStatus.confirmed) {
            log.debug("Booking confirmed. Updating venue availability for venue ID: {}", existingBooking.getVenue().getVenueId());
            Venue venue = venueRepository.findByVenueId(existingBooking.getVenue().getVenueId());
            venue.setAvailabilityStatus(AvailabilityStatus.unavailable);
            venueRepository.save(venue);
        }

        existingBooking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(existingBooking);

        log.info("Booking status updated successfully for ID: {}", bookingId);
        return responseMapper.toDto(updatedBooking, new ArrayList<>());
    }

    @Override
    public void deleteBooking(String bookingId) {
        log.info("Attempting to delete booking ID: {}", bookingId);

        if (!bookingRepository.existsById(bookingId)) {
            log.error("Delete failed: Booking ID {} does not exist", bookingId);
            throw new RuntimeException("Cannot delete: Booking not found with ID: " + bookingId);
        }

        bookingRepository.deleteById(bookingId);
        log.info("Successfully deleted booking ID: {}", bookingId);
    }
}