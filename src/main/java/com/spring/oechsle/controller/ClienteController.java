package com.spring.oechsle.controller;

import com.spring.oechsle.dto.ClienteDTO;
import com.spring.oechsle.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;
    
    @GetMapping(value = "/listclientes")
    public ResponseEntity list(){
        return new ResponseEntity(service.list(), HttpStatus.OK);
    }
    
    @GetMapping(value = "/kpideclientes")
    public ResponseEntity kpideclientes(){
        return new ResponseEntity(service.kpideclientes(), HttpStatus.OK);
    }

    @PostMapping(value = "/creacliente")
    public ResponseEntity add(@RequestBody ClienteDTO post){
        return new ResponseEntity(service.add(post), HttpStatus.OK);
    }

	/*
	 * @PutMapping(value = "/{id}/actualizacliente") public ResponseEntity
	 * edit(@PathVariable(value = "id") String id, @RequestBody ClienteDTO post){
	 * return new ResponseEntity(service.edit(id,post), HttpStatus.OK); }
	 * 
	 * @DeleteMapping(value = "/{id}/eliminacliente") public ResponseEntity
	 * delete(@PathVariable(value = "id") String id){ return new
	 * ResponseEntity(service.delete(id), HttpStatus.OK); }
	 */

}
