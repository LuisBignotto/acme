package br.com.acmeairlines.services;

import br.com.acmeairlines.dtos.FlightCreateDTO;
import br.com.acmeairlines.dtos.FlightDTO;
import br.com.acmeairlines.dtos.FlightUpdateDTO;
import br.com.acmeairlines.models.FlightModel;
import br.com.acmeairlines.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository repository;

    public FlightDTO createFlight(FlightCreateDTO flightCreateDTO) {
        FlightModel flightModel = mapToFlightModel(flightCreateDTO);
        FlightModel savedFlight = repository.save(flightModel);
        return mapToFlightDTO(savedFlight);
    }

    public Optional<FlightDTO> updateFlight(FlightUpdateDTO flightUpdateDTO, Long id) {
        return repository.findById(id)
                .map(flightModel -> {
                    updateFlightModel(flightModel, flightUpdateDTO);
                    FlightModel updatedFlight = repository.save(flightModel);
                    return mapToFlightDTO(updatedFlight);
                });
    }

    public Optional<FlightDTO> getFlightById(Long id) {
        return repository.findById(id).map(this::mapToFlightDTO);
    }

    public Optional<FlightDTO> getFlightByTag(String tag) {
        return repository.findByTag(tag).map(this::mapToFlightDTO);
    }

    public Page<FlightDTO> findAllFlights(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToFlightDTO);
    }

    public void deleteFlight(Long id) {
        repository.deleteById(id);
    }

    private FlightModel mapToFlightModel(FlightCreateDTO flightCreateDTO) {
        return new FlightModel(
                null,
                flightCreateDTO.tag(),
                flightCreateDTO.departureDate(),
                flightCreateDTO.arrivalDate(),
                flightCreateDTO.departureAirport(),
                flightCreateDTO.arrivalAirport(),
                flightCreateDTO.status(),
                flightCreateDTO.airplaneModel());
    }

    private void updateFlightModel(FlightModel flightModel, FlightUpdateDTO flightUpdateDTO) {
        if (flightUpdateDTO.tag() != null) {
            flightModel.setTag(flightUpdateDTO.tag());
        }
        if (flightUpdateDTO.departureDate() != null) {
            flightModel.setDepartureDate(flightUpdateDTO.departureDate());
        }
        if (flightUpdateDTO.arrivalDate() != null) {
            flightModel.setArrivalDate(flightUpdateDTO.arrivalDate());
        }
        if (flightUpdateDTO.departureAirport() != null) {
            flightModel.setDepartureAirport(flightUpdateDTO.departureAirport());
        }
        if (flightUpdateDTO.arrivalAirport() != null) {
            flightModel.setArrivalAirport(flightUpdateDTO.arrivalAirport());
        }
        if (flightUpdateDTO.status() != null) {
            flightModel.setStatus(flightUpdateDTO.status());
        }
        if (flightUpdateDTO.airplaneModel() != null) {
            flightModel.setAirplaneModel(flightUpdateDTO.airplaneModel());
        }
    }

    private FlightDTO mapToFlightDTO(FlightModel flightModel) {
        return new FlightDTO(
                flightModel.getId(),
                flightModel.getTag(),
                flightModel.getDepartureDate(),
                flightModel.getArrivalDate(),
                flightModel.getDepartureAirport(),
                flightModel.getArrivalAirport(),
                flightModel.getStatus(),
                flightModel.getAirplaneModel());
    }
}