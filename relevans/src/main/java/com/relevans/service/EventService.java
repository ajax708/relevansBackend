package com.relevans.service;

import com.relevans.dto.EventDto;
import com.relevans.repo.IEvent;
import com.relevans.repo.model.EventModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final Logger LOGGER = Logger.getLogger(EventService.class.getName());
    private final IEvent eventRepo;
    private final ModelMapper modelMapper;
    private final TaskScheduler taskScheduler;

    @Transactional
    public EventDto save(String session, EventDto eventDto) {
        LOGGER.log(Level.INFO, "[{0}] Saving event: {1}", new Object[]{session, eventDto});
        eventDto.setEstado("Pendiente");
        EventModel eventModel = modelMapper.map(eventDto, EventModel.class);
        eventModel = eventRepo.save(eventModel);
        EventDto savedEventDto = modelMapper.map(eventModel, EventDto.class);
        LOGGER.log(Level.INFO, "[{0}] Event saved: {1}", new Object[]{session, savedEventDto});

        scheduleEventTask(savedEventDto);

        return savedEventDto;
    }

    private void scheduleEventTask(EventDto eventDto) {
        LocalDateTime eventDateTime = eventDto.getFecha();
        Date eventDate = Date.from(eventDateTime.atZone(ZoneId.systemDefault()).toInstant());

        taskScheduler.schedule(() -> {
            LOGGER.log(Level.INFO, "Hola mundo! El evento {0} está ocurriendo ahora!", eventDto.getNombre());
            // Aquí puedes implementar la lógica que quieras que se ejecute

            // Actualizar el estado del evento
            LOGGER.log(Level.INFO, "Actualizando estado del evento {0} a Enviado", eventDto.getNombre());
            Optional<EventModel> eventModelOptional = eventRepo.findById(eventDto.getIdEvento());
            if (eventModelOptional.isPresent()) {
                EventModel eventModel = eventModelOptional.get();
                eventModel.setEstado("Enviado");
                eventRepo.save(eventModel);
                LOGGER.log(Level.INFO, "Estado del evento {0} actualizado a Enviado", eventDto.getNombre());
            } else {
                LOGGER.log(Level.WARNING, "Evento no encontrado: {0}", eventDto.getIdEvento());
            }
        }, eventDate);

        LOGGER.log(Level.INFO, "Scheduled task for event {0} at {1}", new Object[]{eventDto.getNombre(), eventDateTime});
    }

    @Transactional
    public EventDto update(String session, EventDto eventDto) {
        LOGGER.log(Level.INFO, "[{0}] Updating event: {1}", new Object[]{session, eventDto});
        Optional<EventModel> existingEvent = eventRepo.findById(eventDto.getIdEvento());
        if (existingEvent.isEmpty()) {
            throw new EventNotFoundException("Event not found: " + eventDto.getIdEvento());
        }
        EventModel eventModel = modelMapper.map(eventDto, EventModel.class);
        eventModel = eventRepo.save(eventModel);
        EventDto updatedEventDto = modelMapper.map(eventModel, EventDto.class);
        LOGGER.log(Level.INFO, "[{0}] Event updated: {1}", new Object[]{session, updatedEventDto});
        return updatedEventDto;
    }

    public EventDto readById(String session, Integer id) {
        LOGGER.log(Level.INFO, "[{0}] Reading event by ID: {1}", new Object[]{session, id});
        EventModel model = eventRepo.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found: " + id));
        EventDto dto = modelMapper.map(model, EventDto.class);
        LOGGER.log(Level.INFO, "[{0}] Event found: {1}", new Object[]{session, dto});
        return dto;
    }

    public List<EventDto> all(String session) {
        LOGGER.log(Level.INFO, "[{0}] Retrieving all events", new Object[]{session});
        List<EventModel> eventModelList = eventRepo.findAll();
        List<EventDto> eventDtoList = eventModelList.stream()
                .map(model -> modelMapper.map(model, EventDto.class))
                .collect(Collectors.toList());
        LOGGER.log(Level.INFO, "[{0}] Events retrieved: {1}", new Object[]{session, eventDtoList});
        return eventDtoList;
    }

    // Custom exception for event not found
    public static class EventNotFoundException extends RuntimeException {
        public EventNotFoundException(String message) {
            super(message);
        }
    }
}