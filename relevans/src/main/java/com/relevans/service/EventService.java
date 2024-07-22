package com.relevans.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.relevans.dto.EventDto;
import com.relevans.repo.IEvent;
import com.relevans.repo.model.EventModel;
import com.relevans.repo.model.MinisterioModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final NotificationService notificationService;

    // Reprogramar solo eventos pendientes al iniciar el servidor
    @PostConstruct
    public void init() {
        List<EventModel> events = eventRepo.findByEstado("Pendiente");
        events.forEach(this::checkAndScheduleEvent);
    }

    @Transactional
    public EventDto save(String session, EventDto eventDto) {
        LOGGER.log(Level.INFO, "[{0}] Saving event: {1}", new Object[]{session, eventDto});
        eventDto.setEstado("Pendiente");
        EventModel eventModel = modelMapper.map(eventDto, EventModel.class);
        eventModel = eventRepo.save(eventModel);
        EventDto savedEventDto = modelMapper.map(eventModel, EventDto.class);
        LOGGER.log(Level.INFO, "[{0}] Event saved: {1}", new Object[]{session, savedEventDto});

        scheduleEventTask(eventModel);

        return savedEventDto;
    }

    // Comprobar y programar evento
    private void checkAndScheduleEvent(EventModel eventModel) {
        if (eventModel.getFecha().isBefore(LocalDateTime.now())) {
            eventModel.setEstado("Expirado");
            eventRepo.save(eventModel);
            LOGGER.log(Level.INFO, "El evento {0} ha expirado y su estado ha sido actualizado a Expirado", eventModel.getNombre());
        } else {
            scheduleEventTask(eventModel);
        }
    }

    // Programar una tarea para un evento
    private void scheduleEventTask(EventModel eventModel) {
        LocalDateTime eventDateTime = eventModel.getFecha();
        Date eventDate = Date.from(eventDateTime.atZone(ZoneId.systemDefault()).toInstant());

        taskScheduler.schedule(() -> {
            LOGGER.log(Level.INFO, "El evento {0} está ocurriendo ahora!", eventModel.getNombre());

            String title = "Evento en curso";
            String body = "El evento " + eventModel.getNombre() + " está ocurriendo ahora!";
            Map<String, String> data = new HashMap<>();
            data.put("nombre", eventModel.getNombre());
            data.put("descripcion", eventModel.getDescripcion());
            data.put("hora", eventModel.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            Set<MinisterioModel> ministerios = eventModel.getMinisterios();
            if (ministerios.isEmpty()) {
                // Enviar a todos los dispositivos
                try {
                    notificationService.sendNotificationToAllDevices(title, body, data);
                } catch (FirebaseMessagingException e) {
                    LOGGER.log(Level.SEVERE, "Error enviando notificación para el evento {0}: {1}", new Object[]{eventModel.getNombre(), e.getMessage()});
                }
            } else {
                // Enviar a tópicos específicos
                for (MinisterioModel ministerio : ministerios) {
                    String topic = "ministerio_" + ministerio.getIdMinisterio(); // Tópico basado en el id del ministerio
                    try {
                        notificationService.sendNotificationToTopic(topic, title, body, data);
                    } catch (FirebaseMessagingException e) {
                        LOGGER.log(Level.SEVERE, "Error enviando notificación para el evento {0} y ministerio {1}: {2}", new Object[]{eventModel.getNombre(), ministerio.getNombre(), e.getMessage()});
                    }
                }
            }

            // Actualizar el estado del evento
            LOGGER.log(Level.INFO, "Actualizando estado del evento {0} a Enviado", eventModel.getNombre());
            eventModel.setEstado("Enviado");
            eventRepo.save(eventModel);
            LOGGER.log(Level.INFO, "Estado del evento {0} actualizado a Enviado", eventModel.getNombre());
        }, eventDate);

        LOGGER.log(Level.INFO, "Scheduled task for event {0} at {1}", new Object[]{eventModel.getNombre(), eventDateTime});
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