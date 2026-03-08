package com.cts.eventsphere.service.impl;

import com.cts.eventsphere.dto.booking.BookingRequestDto;
import com.cts.eventsphere.dto.booking.BookingResponseDto;
import com.cts.eventsphere.dto.mapper.booking.BookingRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.booking.BookingResponseDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueRequestDtoMapper;
import com.cts.eventsphere.dto.mapper.venue.VenueResponseDtoMapper;
import com.cts.eventsphere.dto.resource.ResourceListElementDto;
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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final ResourceAllocationRepository resourceAllocationRepository;


    private final ResourceServiceImpl resourceService;

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

//        resourceService.requestAllocation(bookingRequestDto.,bookingRequestDto.eventId(), bookingRequestDto.venueId(),bookingRequestDto.resourceList());

        // Save to Database
        booking.setDate(LocalDate.now());
        Booking savedBooking = bookingRepository.save(booking);

        // Return mapped Response DTO
        return responseMapper.toDto(savedBooking,new ArrayList<>());
    }



    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(booking -> {
                    // 1. Fetch resources for this specific event/booking
                    List<ResourceListElementDto> resources = resourceAllocationRepository
                            .findByEvent_EventId(booking.getEventId())
                            .stream()
                            .map(allocation -> new ResourceListElementDto(
                                    allocation.getResource().getName(),
                                    allocation.getQuantity()
                            ))
                            .toList();

                    // 2. Use the mapper with both required arguments
                    return responseMapper.toDto(booking, resources);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByEvent(String eventId) {
        // 1. Fetch all bookings associated with the event
        List<Booking> bookings = bookingRepository.findByEventId(eventId);

        // 2. Fetch all resource allocations for this event to map them later
        List<ResourceAllocation> allocations = resourceAllocationRepository.findByEvent_EventId(eventId);

        // 3. Map the Entities to Response DTOs
        return bookings.stream().map(booking -> {

            // Filter allocations that belong specifically to this booking/venue if needed
            List<ResourceListElementDto> resourceList = allocations.stream()
                    .filter(a -> a.getEvent().getVenueId().equals(booking.getVenueId())) // Optional: filter by venue logic
                    .map(a -> new ResourceListElementDto(
                            a.getResource().getName(),
                            a.getResource().getUnit()
                    ))
                    .toList();

            // Return the final record
            return new BookingResponseDto(
                    booking.getBookingId(),
                    booking.getEventId(),
                    booking.getVenueId(),
                    booking.getDate(),
                    booking.getStatus(),
                    resourceList,
                    booking.getCreatedAt(),
                    booking.getUpdatedAt()
            );
        }).toList();
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

        return responseMapper.toDto(updatedBooking,new ArrayList<>());
    }

    @Override
    public void deleteBooking(String bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new RuntimeException("Cannot delete: Booking not found with ID: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }
}