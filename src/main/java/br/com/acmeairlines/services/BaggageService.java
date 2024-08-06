package br.com.acmeairlines.services;

import br.com.acmeairlines.dtos.BaggageDTO;
import br.com.acmeairlines.dtos.BaggageTrackerDTO;
import br.com.acmeairlines.dtos.CreateBaggageDTO;
import br.com.acmeairlines.dtos.StatusDTO;
import br.com.acmeairlines.models.BaggageModel;
import br.com.acmeairlines.models.BaggageTrackerModel;
import br.com.acmeairlines.models.StatusModel;
import br.com.acmeairlines.repositories.BaggageRepository;
import br.com.acmeairlines.repositories.StatusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaggageService {

    private final BaggageRepository baggageRepository;
    private final StatusRepository statusRepository;

    public BaggageService(BaggageRepository baggageRepository, StatusRepository statusRepository) {
        this.baggageRepository = baggageRepository;
        this.statusRepository = statusRepository;
    }

    public Page<BaggageDTO> findAllBaggages(Pageable pageable) {
        return baggageRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    public BaggageDTO createBaggage(CreateBaggageDTO createBaggageDTO) {
        StatusModel status = statusRepository.findById(createBaggageDTO.statusId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));

        BaggageModel baggageModel = new BaggageModel();
        baggageModel.setUserId(createBaggageDTO.userId());
        baggageModel.setTag(createBaggageDTO.tag());
        baggageModel.setColor(createBaggageDTO.color());
        baggageModel.setWeight(createBaggageDTO.weight());
        baggageModel.setStatus(status);
        baggageModel.setLastLocation(createBaggageDTO.lastLocation());
        baggageModel.setFlightId(createBaggageDTO.flightId());

        List<BaggageTrackerModel> trackers = createBaggageDTO.trackers().stream()
                .map(trackerDTO -> {
                    BaggageTrackerModel trackerModel = new BaggageTrackerModel();
                    trackerModel.setBaggage(baggageModel);
                    trackerModel.setTrackerUserId(trackerDTO.trackerUserId());
                    return trackerModel;
                })
                .collect(Collectors.toList());

        baggageModel.setTrackers(trackers);

        BaggageModel savedBaggage = baggageRepository.save(baggageModel);
        return convertToDTO(savedBaggage);
    }

    @Transactional(readOnly = true)
    public List<BaggageDTO> getBaggagesByUserId(Long userId) {
        List<BaggageModel> baggageModels = baggageRepository.findByUserId(userId);
        return baggageModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BaggageDTO> getBaggagesByFlightId(Long flightId) {
        List<BaggageModel> baggageModels = baggageRepository.findByFlightId(flightId);
        return baggageModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BaggageDTO getBaggageByTag(String tag) {
        BaggageModel baggageModel = baggageRepository.findByTag(tag);
        return baggageModel != null ? convertToDTO(baggageModel) : null;
    }

    @Transactional(readOnly = true)
    public List<BaggageDTO> getBaggagesByTrackerUserId(Long trackerUserId) {
        List<BaggageModel> baggageModels = baggageRepository.findByTrackersTrackerUserId(trackerUserId);
        return baggageModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteBaggage(Long baggageId) {
        baggageRepository.deleteById(baggageId);
    }

    @Transactional
    public BaggageDTO updateBaggage(Long baggageId, BaggageDTO baggageDTO) {
        BaggageModel baggageModel = baggageRepository.findById(baggageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid baggage ID"));

        if (baggageDTO.userId() != null) {
            baggageModel.setUserId(baggageDTO.userId());
        }
        if (baggageDTO.tag() != null) {
            baggageModel.setTag(baggageDTO.tag());
        }
        if (baggageDTO.color() != null) {
            baggageModel.setColor(baggageDTO.color());
        }
        if (baggageDTO.weight() != null) {
            baggageModel.setWeight(baggageDTO.weight());
        }
        if (baggageDTO.status() != null && baggageDTO.status().id() != null) {
            StatusModel status = statusRepository.findById(baggageDTO.status().id())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status ID"));
            baggageModel.setStatus(status);
        }
        if (baggageDTO.lastLocation() != null) {
            baggageModel.setLastLocation(baggageDTO.lastLocation());
        }
        if (baggageDTO.flightId() != null) {
            baggageModel.setFlightId(baggageDTO.flightId());
        }

        if (baggageDTO.trackers() != null) {
            baggageModel.getTrackers().clear();
            baggageDTO.trackers().forEach(trackerDTO -> {
                BaggageTrackerModel trackerModel = new BaggageTrackerModel();
                trackerModel.setBaggage(baggageModel);
                trackerModel.setTrackerUserId(trackerDTO.trackerUserId());
                baggageModel.getTrackers().add(trackerModel);
            });
        }

        BaggageModel updatedBaggage = baggageRepository.save(baggageModel);
        return convertToDTO(updatedBaggage);
    }

    private BaggageDTO convertToDTO(BaggageModel baggageModel) {
        StatusDTO statusDTO = new StatusDTO(baggageModel.getStatus().getId(), baggageModel.getStatus().getStatus());

        List<BaggageTrackerDTO> trackerDTOs = baggageModel.getTrackers().stream()
                .map(trackerModel -> new BaggageTrackerDTO(trackerModel.getId(), trackerModel.getBaggage().getId(), trackerModel.getTrackerUserId()))
                .collect(Collectors.toList());

        return new BaggageDTO(
                baggageModel.getId(),
                baggageModel.getUserId(),
                baggageModel.getTag(),
                baggageModel.getColor(),
                baggageModel.getWeight(),
                statusDTO,
                baggageModel.getLastLocation(),
                baggageModel.getFlightId(),
                trackerDTOs
        );
    }
}