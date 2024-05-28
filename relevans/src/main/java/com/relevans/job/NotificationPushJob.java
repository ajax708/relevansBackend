package com.relevans.job;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.relevans.dto.EventDto;
import com.relevans.repo.model.EventModel;
import com.relevans.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationPushJob {
    private final EventService eventService;

    @Autowired
    public NotificationPushJob(EventService eventService) {
        this.eventService = eventService;
    }

    @Scheduled(fixedRate = 300000) // Esto se ejecutará cada 5 minutos
    public void performTask() throws FirebaseMessagingException {

        // Obtén todos los eventos
        List<EventDto> events = eventService.all("");

        // Obtén la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();

        List<EventDto> futureEvents = events.stream()
                .filter(event -> event.getFechaRegistro().isAfter(now))
                .collect(Collectors.toList());
        if(futureEvents.isEmpty()){
            System.out.println("No se encontraron eventos próximos.");
        }
        //sendNotificationToAllDevices("Prueba", "Prueba de notificación");
        // Itera sobre los eventos
        for (EventDto event : futureEvents) {
            // Compara la fecha del evento con la fecha y hora actual
            long minutesUntilEvent = ChronoUnit.MINUTES.between(now, event.getFechaRegistro());

            // Si el evento está próximo (por ejemplo, en los próximos 30 minutos), envía una notificación
            if (minutesUntilEvent <= 30) {

                sendNotificationToAllDevices("Evento próximo", "El evento " + event.getNombre() + " está próximo.");
                System.out.println("El evento " + event.getFechaRegistro() + " está próximo.");
            }else{
                System.out.println("No se encontraron eventos próximos.");
            }
        }
    }
    public void sendNotificationToAllDevices(String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setTopic("all")
                .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
