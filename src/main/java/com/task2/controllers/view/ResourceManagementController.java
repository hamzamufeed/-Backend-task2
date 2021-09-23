package com.task2.controllers.view;

import com.task2.controllers.DTOs.ServerDTO;
import com.task2.services.ResourceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResourceManagementController {

    @Autowired
    private ResourceManagementService resourceManagementService;

    @GetMapping("/server/")
    public List<ServerDTO> getAll(){
        return resourceManagementService.getAllServers();
    }

    @GetMapping("/server/{id}")
    public ResponseEntity<?> get(@PathVariable int id){
        ServerDTO serverDTO = resourceManagementService.getServer(id);
        return (serverDTO == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server Not Found") :
                ResponseEntity.status(HttpStatus.OK).body(serverDTO);
    }

    @PostMapping("/server/total")
    public ResponseEntity<?> CreateServer(@RequestParam() Double size) {
        if (size <= 0 || size > 100)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Memory Size should be 0 - 100");
        else {
            ServerDTO newServer = resourceManagementService.addServer(size);
            return ResponseEntity.status(HttpStatus.CREATED).body(newServer);
        }
    }

    @PutMapping("/server/{id}")
    public ResponseEntity<?> update(@RequestBody ServerDTO serverDTO, @PathVariable("id") Integer id) {
        if (resourceManagementService.getServer(serverDTO.getServerId()) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Not Found");
        else if (serverDTO.getServerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Not Valid");
        else if (serverDTO.getTotal() <= 0 || serverDTO.getTotal() > 100)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Memory Size should be 0 - 100");
        else
            return ResponseEntity.status(HttpStatus.OK).body(resourceManagementService.updateServer(serverDTO));
    }

    @DeleteMapping("/server/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        ServerDTO serverDTO = resourceManagementService.getServer(id);
        if (serverDTO == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server Not Found");
        else {
            resourceManagementService.removeServer(id);
            return ResponseEntity.status(HttpStatus.OK).body("Server Deleted");
        }
    }

    @PostMapping("/server/allocate")
    public ResponseEntity<?> AllocateServer(@RequestParam() Double size) {
        if(size <= 0 || size > 100)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Allocation should be 0 - 100");
        return ResponseEntity.status(HttpStatus.OK).body(resourceManagementService.allocateServer(size));
    }

}
