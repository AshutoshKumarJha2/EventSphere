package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.booking.BookingResponseVenueManagerDto;
import com.cts.eventsphere.dto.mapper.booking.BookingRepsonseVenueManagerDtoMapper;
import com.cts.eventsphere.dto.mapper.booking.BookingRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.booking.BookingResponseDtoMapper;
import com.cts.eventsphere.dto.resource.ResourceListElementDto;
import com.cts.eventsphere.dto.resource.ResourceVenueManagerResponseDto;
import com.cts.eventsphere.exception.booking.BookingNotFoundException;
import com.cts.eventsphere.exception.resource.InsufficientResourceException;
import com.cts.eventsphere.exception.venue.VenueNotFoundException;
import com.cts.eventsphere.model.*;
import com.cts.eventsphere.model.data.AvailabilityStatus;
import com.cts.eventsphere.model.data.BookingStatus;
import com.cts.eventsphere.repository.*;
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
 * Service Implementation for Booking operations.
 * Handles the logic for creating, updating, and retrieving booking records.
 * * @author 2479476
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
    private final BookingRepsonseVenueManagerDtoMapper bookingRepsonseVenueManagerDtoMapper;
    private final EventRepository eventRepository;
    private final ResourceRepository resourceRepository;

    /**
     * Creates a new booking and associates it with a venue.
     * * @param bookingRequestDto the details of the booking request
     * @return the created booking details as a response DTO
     * @throws VenueNotFoundException if the specified venue ID does not exist
     */
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

    /**
     * Retrieves all bookings with their associated resource allocations.
     * * @return a list of all booking response DTOs
     */
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

    /**
     * Retrieves all bookings for a specific venue ID.
     * * @param venueId the unique identifier of the venue
     * @return a list of bookings associated with the venue
     */
    @Override
    public List<BookingResponseVenueManagerDto> getBookingsByVenue(String venueId) {
        log.info("Fetching bookings for venue ID: {}", venueId);

        return bookingRepository.findByVenue_VenueId(venueId)
                .stream()
                .map(booking -> {
                    List<ResourceVenueManagerResponseDto> resources = resourceAllocationRepository
                            .findByEvent_EventId(booking.getEventId())
                            .stream()
                            .map(a -> new ResourceVenueManagerResponseDto(a.getResource().getName(), a.getQuantity()))
                            .toList();

                    return bookingRepsonseVenueManagerDtoMapper.toDto(booking, resources);
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves bookings associated with a specific event ID.
     * * @param eventId the unique identifier of the event
     * @return a list of booking response DTOs for the event
     */
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

    /**
     * Updates the status of a booking and handles resource inventory deduction upon confirmation.
     * * @param bookingId the unique identifier of the booking
     * @param newStatus the new status to apply
     * @return the updated booking details
     * @throws BookingNotFoundException if the booking ID is not found
     * @throws InsufficientResourceException if resources are unavailable for confirmation
     */
    @Override
    @Transactional
    public BookingResponseDto updateBookingStatus(String bookingId, BookingStatus newStatus) {
        log.info("Updating status for booking ID: {} to {}", bookingId, newStatus);

        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking ID " + bookingId + " not found."));

        if (newStatus == BookingStatus.confirmed) {
            Venue venue = existingBooking.getVenue();
            venue.setAvailabilityStatus(AvailabilityStatus.unavailable);
            venueRepository.save(venue);

            List<ResourceAllocation> allocations = resourceAllocationRepository
                    .findByEvent_EventIdAndVenue_VenueId(existingBooking.getEventId(), venue.getVenueId());

            for (ResourceAllocation allocation : allocations) {
                Resource resource = allocation.getResource();
                int requestedQty = allocation.getQuantity();
                int availableQty = resource.getUnit();

                if (availableQty < requestedQty) {
                    throw new InsufficientResourceException("Not enough " + resource.getName() + " available.");
                }

                resource.setUnit(availableQty - requestedQty);
                resourceRepository.save(resource);

                log.debug("Subtracted {} from resource {}. New balance: {}",
                        requestedQty, resource.getName(), resource.getUnit());
            }
        }

        existingBooking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(existingBooking);

        log.info("Booking status updated successfully for ID: {}", bookingId);
        return responseMapper.toDto(updatedBooking, new ArrayList<>());
    }

    /**
     * Deletes a booking record based on the provided ID.
     * * @param bookingId the unique identifier of the booking to delete
     * @throws RuntimeException if the booking does not exist
     */
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