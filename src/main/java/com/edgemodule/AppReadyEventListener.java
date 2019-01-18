package com.edgemodule;

import com.edgemodule.IotEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {



    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //Create IotEdge Class
        IotEdge iotEdge = new IotEdge();

        // Start IotEdge Bus
        iotEdge.start();

    }

}
