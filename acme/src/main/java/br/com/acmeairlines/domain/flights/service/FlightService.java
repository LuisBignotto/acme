package br.com.acmeairlines.domain.flights.service;

import br.com.acmeairlines.domain.flights.dto.FlightDataDTO;
import br.com.acmeairlines.domain.flights.dto.FlightUpdateDTO;
import br.com.acmeairlines.domain.flights.model.FlightModel;
import br.com.acmeairlines.domain.flights.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlightService {

    @Autowired
    private FlightRepository repository;

    public FlightModel createFlight(@Valid FlightDataDTO data) {
        return repository.save(new FlightModel(UUID.randomUUID().toString(), data.flightNumber(), data.departureDate(), data.arrivalDate(), data.departureAirport(), data.arrivalAirport()));
    }

    public FlightModel updateFlight(@Valid FlightUpdateDTO data, String id) {
        FlightModel flight = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("None flight were found using id: " + id));

        if (data.departureDate() != null) {
            flight.setDepartureDate(data.departureDate());
        }

        if (data.arrivalDate() != null) {
            flight.setArrivalDate(data.arrivalDate());
        }

        if (data.departureAirport() != null) {
            flight.setDepartureAirport(data.departureAirport());
        }

        if (data.arrivalAirport() != null) {
            if (!data.arrivalAirport().equalsIgnoreCase(flight.getArrivalAirport()) || data.departureAirport() != null && !data.departureAirport().equalsIgnoreCase(data.arrivalAirport())) {
                flight.setArrivalAirport(data.arrivalAirport());
            }
        }

        return repository.save(flight);
    }

    public FlightDataDTO getFlight(String id) {
        return new FlightDataDTO(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("None flight were found using id: " + id)));
    }

    public FlightDataDTO getFlightByTag(String tag) {
        return new FlightDataDTO(repository.findByFlightNumber(tag));
    }

    public Page<FlightModel> findAllFlights(Pageable pages) {
        return repository.findAll(pages);
    }

    public void deleteFlight(String id) {
        FlightModel flight = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("None flight were found using id: " + id));
        repository.delete(flight);
    }
}