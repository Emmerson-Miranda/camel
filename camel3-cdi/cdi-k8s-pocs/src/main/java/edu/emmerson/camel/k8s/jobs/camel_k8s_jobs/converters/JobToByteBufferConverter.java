package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs.converters;

import java.nio.ByteBuffer;

import org.apache.camel.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(generateLoader = true)
public class JobToByteBufferConverter {

    @Converter
    public static ByteBuffer toInputStream(io.fabric8.kubernetes.api.model.batch.Job  j) throws JsonProcessingException  {
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] byteArray = objectMapper.writeValueAsBytes(j);
		return ByteBuffer.wrap(byteArray);
    }
	
}
