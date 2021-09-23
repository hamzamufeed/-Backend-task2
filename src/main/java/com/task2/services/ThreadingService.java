package com.task2.services;

import com.task2.DB.AerospikeServerRepository;
import com.task2.DB.ServerCache;
import com.task2.DB.ServerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadingService implements Runnable{

    private Thread thread;
    private String threadName;
    private ServerModel serverModel;
    private AerospikeServerRepository aerospikeServerRepository;

    public final Logger logger = LogManager.getLogger(TransformerService.class);

    public ThreadingService(){

    }

    public ThreadingService(ServerModel serverModel, AerospikeServerRepository aerospikeServerRepository){
        this.serverModel = serverModel;
        this.threadName = "Server - "+serverModel.getServerId();
        this.aerospikeServerRepository = aerospikeServerRepository;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, this.threadName);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            logger.info("Creating "+ this.threadName);
            Thread.sleep(20000);
            this.serverModel.setState("Active");
            logger.info(this.threadName + " is Active");
            this.aerospikeServerRepository.save(serverModel);
            ServerCache.getInstance().update(serverModel.getServerId(), serverModel);
        } catch (InterruptedException e) {
            logger.info(threadName + " Interrupted.");
        }
        logger.info("Done.");
    }
}
