package com.spring.oechsle.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.spring.oechsle.dto.ClienteDTO;
import com.spring.oechsle.dto.ClienteKpiDTO;
import com.spring.oechsle.firebase.FirebaseInitializer;
import com.spring.oechsle.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import java.text.SimpleDateFormat;


@Service
public class ClienteServiceImpl implements ClienteService {

	public static final double ESPERANZA_VIDA_PERU = 75.22;
	
    @Autowired
    private FirebaseInitializer firebase;

    @Override
    public List<ClienteDTO> list() {
        List<ClienteDTO> response = new ArrayList<>();
        ClienteDTO post;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
        	
        	int sum = 0;
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(ClienteDTO.class);
                post.setId(doc.getId());          
                //post.setFechaProbableMuerte(calcularFechaDeceso(Date.parse(post.getFechaNacimiento())));
                
                Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(post.getFechaNacimiento());
                Date date2 = calcularFechaDeceso(date1);
                post.setFechaProbableMuerte(date2);
                response.add(post);
            }
            
            return response;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public ClienteKpiDTO kpideclientes() {
        List<ClienteDTO> response = new ArrayList<>();
        ClienteKpiDTO response2 = new ClienteKpiDTO();
        ClienteDTO post;
        ClienteDTO post2;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
        	
        	double sum = 0;
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                post = doc.toObject(ClienteDTO.class);
                post.setId(doc.getId());                
                sum = sum + Double.parseDouble(post.getEdad());           
                response.add(post);
            }
            
            //calculo edad promedio de los clientes
            double promedio = sum/response.size();
            
            //calculo de la varianza
            double sumatoria = 0;
            double varianza = 0;
            double desviacion = 0;
            
            for (DocumentSnapshot doc2 : querySnapshotApiFuture.get().getDocuments()) {    
            	post2 = doc2.toObject(ClienteDTO.class);
            	sumatoria = Math.pow(Double.parseDouble(post2.getEdad()) - promedio, 2);
            	varianza = varianza + sumatoria;
            }            
            varianza = varianza/response.size();
            
            //calculo de la Desviacion standar
            desviacion = Math.sqrt(varianza);
            
            response2.setPromedio(promedio);
            response2.setVarianza(varianza);
            response2.setDesviacion(desviacion);
            
            return response2;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean add(ClienteDTO post) {
        Map<String, Object> docData = getDocData(post);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

        try {
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }


    @Override
    public Boolean edit(String id, ClienteDTO post) {
        Map<String, Object> docData = getDocData(post);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(docData);
        try {
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean delete(String id) {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
        try {
            if(null != writeResultApiFuture.get()){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("cliente");
    }

    private Map<String, Object> getDocData(ClienteDTO post) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("nombre", post.getNombre());
        docData.put("apellido", post.getApellido());
        docData.put("edad", post.getEdad());
        docData.put("fechaNacimiento", post.getFechaNacimiento());
        return docData;
    }
    
    public static Date calcularFechaDeceso(Date fechaNacimiento) {
		LocalDate localdate = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int añosCliente = Period.between(localdate, LocalDate.now()).getYears(); 
		Double anosRestante = (ESPERANZA_VIDA_PERU - añosCliente);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, anosRestante.intValue());
		
		return calendar.getTime();

	}
}

