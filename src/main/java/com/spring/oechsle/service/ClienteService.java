package com.spring.oechsle.service;

import java.util.List;

import com.spring.oechsle.dto.ClienteDTO;
import com.spring.oechsle.dto.ClienteKpiDTO;

public interface ClienteService {

    List<ClienteDTO> list();

    Boolean add(ClienteDTO post);

    Boolean edit(String id,ClienteDTO post);

    Boolean delete(String id);

    ClienteKpiDTO kpideclientes();

}
