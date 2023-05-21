package main.controllers;

import lombok.extern.slf4j.Slf4j;
import main.models.IceCreamDTO;
import main.services.IceCreamService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class IceCreamController {

    public static final String ICE_CREAM_PATH = "/api/v1/iceCream";
    public static final String ICE_CREAM_PATH_ID = ICE_CREAM_PATH + "/{iceCreamId}";

    private final IceCreamService iceCreamService;

    public IceCreamController(IceCreamService iceCreamService) {
        this.iceCreamService = iceCreamService;
    }

    // that only works for the IceCreamController and does not handle the customer controller, It's only meant for local use.
    /* @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return ResponseEntity.notFound().build();
    }*/

    @GetMapping(value = ICE_CREAM_PATH_ID)
    public IceCreamDTO getIceCreamById(@PathVariable("iceCreamId") UUID iceCreamId){
        log.debug("GET IceCreamById in IceCreamController was called.");
        return iceCreamService.getIceCreamById(iceCreamId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(value = ICE_CREAM_PATH)
    public List<IceCreamDTO> getAllIceCreams(){
        log.debug("GET_ALL IceCreams in IceCreamController was called.");
        return iceCreamService.getAllIceCreams();
    }

    @PostMapping(value = ICE_CREAM_PATH)
    public ResponseEntity addNewIceCream(@Validated @RequestBody IceCreamDTO iceCream){
        log.debug("POST IceCream in IceCreamController was called.");
        IceCreamDTO savedIceCream = iceCreamService.saveNewIceCream(iceCream);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", ICE_CREAM_PATH + savedIceCream.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = ICE_CREAM_PATH_ID)
    public ResponseEntity updateIceCreamById(@PathVariable("iceCreamId") UUID iceCreamId, @RequestBody IceCreamDTO iceCream){
        log.debug("PUT IceCreamById in IceCreamController was called.");

        if(iceCreamService.updateIceCreamById(iceCreamId, iceCream).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = ICE_CREAM_PATH_ID)
    public ResponseEntity updatePatchIceCreamById(@PathVariable("iceCreamId") UUID iceCreamId, @RequestBody IceCreamDTO iceCream){
        log.debug("PATCH IceCreamById in IceCreamController was called.");
        iceCreamService.patchIceCreamById(iceCreamId, iceCream);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = ICE_CREAM_PATH_ID)
    public ResponseEntity deleteIceCreamById(@PathVariable("iceCreamId") UUID iceCreamId){
        log.debug("DELETE IceCreamById in IceCreamController was called.");
        if(!iceCreamService.deleteIceCreamById(iceCreamId)){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
