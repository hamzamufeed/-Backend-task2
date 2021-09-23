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
        return resourceManagementService.getAll();
    }

    @GetMapping("/server/{id}")
    public ResponseEntity<?> get(@PathVariable int id){
        ServerDTO serverDTO = resourceManagementService.get(id);
        return (serverDTO == null) ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server Not Found") :
                ResponseEntity.status(HttpStatus.OK).body(serverDTO);
    }

    @PostMapping("/server")
    public ResponseEntity<?> CreateServer(@RequestBody ServerDTO serverDTO) {
        if (resourceManagementService.get(serverDTO.getServerId()) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Already Exists");
        else if (serverDTO.getServerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Not Valid");
        else if (serverDTO.getTotal() <= 0 || serverDTO.getTotal() > 100)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Memory Size should be 0 - 100");
        else {
            resourceManagementService.add(serverDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Server Added");
        }
    }

    @PutMapping("/server/{id}")
    public ResponseEntity<?> update(@RequestBody ServerDTO serverDTO, @PathVariable("id") Integer id) {
        if (resourceManagementService.get(serverDTO.getServerId()) == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Not Found");
        else if (serverDTO.getServerId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Not Valid");
        else if (serverDTO.getTotal() <= 0 || serverDTO.getTotal() > 100)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Memory Size should be 0 - 100");
        else
            return ResponseEntity.status(HttpStatus.OK).body(resourceManagementService.update(serverDTO));
    }

    @DeleteMapping("/server/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        ServerDTO serverDTO = resourceManagementService.get(id);
        if (serverDTO == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server Not Found");
        else {
            resourceManagementService.remove(id);
            return ResponseEntity.status(HttpStatus.OK).body("Server Deleted");
        }
    }

    @PostMapping("/server/allocate")
    public ResponseEntity<?> AllocateServer(@RequestParam() Double size) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceManagementService.allocate(size));
    }

}
