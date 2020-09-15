package edu.emmerson.camel.k8s.jobs.camel_k8s_jobs.converters;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.apache.camel.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.fabric8.kubernetes.api.model.batch.Job;

@Converter(generateLoader = true)
public class ArrayListToByteBufferConverter {

    @Converter
    public static ByteBuffer toInputStream(ArrayList<Job> al) throws JsonProcessingException  {
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] byteArray = objectMapper.writeValueAsBytes(al);
		return ByteBuffer.wrap(byteArray);
    }
	
}
