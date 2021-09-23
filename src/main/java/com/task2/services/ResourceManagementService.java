package com.task2.services;

import com.task2.DB.AerospikeServerRepository;
import com.task2.DB.ServerCache;
import com.task2.DB.ServerModel;
import com.task2.controllers.DTOs.ServerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ResourceManagementService {

    @Autowired
    AerospikeServerRepository aerospikeServerRepository;

    @Autowired
    TransformerService transformerService;

    @PostConstruct
    public void initializeCacheFromDB(){
        ServerCache.getInstance().setServers(
                StreamSupport
                        .stream(aerospikeServerRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList())
        );
    }

    public List<ServerDTO> getAllServers() {
        ArrayList<ServerModel> allServers = new ArrayList<>(ServerCache.getInstance().getServers().values());
        return StreamSupport
                .stream(allServers.spliterator(), false)
                .map( i -> (ServerDTO) transformerService.EntityToDto(i, ServerDTO.class))
                .collect(Collectors.toList());
    }

    public ServerDTO getServer(int id) {
        ServerModel serverModel = ServerCache.getInstance().read(id);
        return (serverModel != null) ? (ServerDTO) transformerService.EntityToDto(serverModel, ServerDTO.class) : null;
    }

    public ServerDTO addServer(Double size) {
        ServerDTO serverDTO = new ServerDTO();
        serverDTO.setServerId(getNextId());
        serverDTO.setTotal(size);
        Date date = new Date();
        serverDTO.setAllocated(0.0);
        serverDTO.setFree(size);
        serverDTO.setState("Creating");
        serverDTO.setDateCreated(date);
        ServerModel serverModel = (ServerModel) transformerService.DtoToEntity(serverDTO, ServerModel.class);

        ThreadingService threadingService = new ThreadingService(serverModel,aerospikeServerRepository);
        threadingService.start();

        aerospikeServerRepository.save(serverModel);
        ServerCache.getInstance().write(serverModel);
        return (ServerDTO) transformerService.EntityToDto(serverModel, ServerDTO.class);
    }

    public ServerDTO updateServer(ServerDTO serverDTO) {
        ServerModel updatedServerModel = (ServerModel) transformerService.DtoToEntity(serverDTO, ServerModel.class);
        aerospikeServerRepository.save(updatedServerModel);
        ServerCache.getInstance().update(serverDTO.getServerId(), updatedServerModel);
        return (ServerDTO) transformerService.EntityToDto(updatedServerModel, ServerDTO.class);
    }

    public void removeServer(Integer id) {
        aerospikeServerRepository.deleteById(id);
        ServerCache.getInstance().delete(id);
    }

    public ServerDTO allocateServer(Double size) {
        ServerModel serverToAllocate = getServerToAllocate(size);
        if(serverToAllocate != null) {
            serverToAllocate.setAllocated(serverToAllocate.getAllocated() + size);
            serverToAllocate.setFree(serverToAllocate.getTotal() - serverToAllocate.getAllocated());
            ServerCache.getInstance().update(serverToAllocate.getServerId(), serverToAllocate);
            aerospikeServerRepository.save(serverToAllocate);
            return (ServerDTO) transformerService.EntityToDto(serverToAllocate, ServerDTO.class);
        }
        else {
            return addServer(size);
        }
    }

    public static Integer getNextId(){
        ArrayList<ServerModel> allServers = new ArrayList<>(ServerCache.getInstance().getServers().values());
        if(allServers.isEmpty())
            return 1;
        Integer max = StreamSupport
                .stream(allServers.spliterator(), false)
                .mapToInt((x) -> x.getServerId()).summaryStatistics().getMax();
        return ++max;
    }

    public static ServerModel getServerToAllocate(Double size){
        ArrayList<ServerModel> allServers = new ArrayList<>(ServerCache.getInstance().getServers().values());
        try {
            return StreamSupport
                    .stream(allServers.spliterator(), false)
                    .filter(i -> i.getFree() >= size && i.getState().equals("Active"))
                    .sorted(Comparator.comparing((ServerModel::getFree)))
                    .collect(Collectors.toList())
                    .get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
