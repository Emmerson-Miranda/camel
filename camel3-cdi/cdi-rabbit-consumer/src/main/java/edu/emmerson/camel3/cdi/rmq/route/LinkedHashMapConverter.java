package edu.emmerson.camel3.cdi.rmq.route;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.camel.Converter;

@Converter
public class LinkedHashMapConverter {

	private static final int BSIZE = 1024;
	
    @Converter
    public static ByteBuffer toByteBuffer(java.util.LinkedHashMap map) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("{");
    	sb.append(toJsonString(map));
    	sb.append("}");
        return ByteBuffer.wrap(sb.toString().getBytes());
    }
    
    private static StringBuilder toJsonString(java.util.LinkedHashMap map) {
    	StringBuilder sb = new StringBuilder();
    	
    	Iterator it = map.entrySet().iterator();
    	while(it.hasNext()) {
    		Entry o = (Entry) it.next();
    		//System.out.println(o.getKey());
    		//System.out.println(o.getValue());
    		
    		sb.append("\"").append(o.getKey()).append("\"").append(":");
    		sb.append("\"").append(o.getValue()).append("\"");
    		
    		if(it.hasNext()) {
    			sb.append(",");
    		}
    	}
    	
    	return sb;
    }
    
}
