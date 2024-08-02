package com.relevans.job;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.relevans.repo.IEvent;
import com.relevans.repo.model.EventModel;
import com.relevans.repo.model.MinisterioModel;
import com.relevans.service.NotificationService;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(EventTask.class.getName());
    private final EventModel eventModel;
    private final NotificationService notificationService;
    private final IEvent eventRepo;

    public EventTask(EventModel eventModel, NotificationService notificationService, IEvent eventRepo) {
        this.eventModel = eventModel;
        this.notificationService = notificationService;
        this.eventRepo = eventRepo;
    }

    @Override
    public void run() {
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
    }
}
