package ar.com.bia;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ar.com.bia.dto.druggability.ScoreDTO;


public class MD5 {
	
	public static MessageDigest instance = null;
	
	private static final ObjectMapper SORTED_MAPPER = new ObjectMapper();
	static {
	    SORTED_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
	}
	
	private static String convertNode(String data) throws IOException {
		ScoreDTO obj = SORTED_MAPPER.readValue(data,ScoreDTO.class);
		obj.setFilters( obj.getFilters().stream().sorted((x,y) -> x.getName().compareTo(y.getName())  ).collect(Collectors.toList()) );
		obj.setScores( obj.getScores().stream().sorted((x,y) -> x.getName().compareTo(y.getName())).collect(Collectors.toList()) );
	    final String json = SORTED_MAPPER.writeValueAsString(obj);
	    return json;
	}
	
	public static String createCriteriaFromQueryStringColumns(HttpServletRequest request) {

		@SuppressWarnings("unchecked")
		Collection<String> columnParams = new ArrayList<String>(request.getParameterMap().keySet());
		CollectionUtils.filter(columnParams, new Predicate() {
			public boolean evaluate(Object arg0) {
				return ((String) arg0).startsWith("columns");
			}
		});
		int columnCount = columnParams.size() / 6;
		StringJoiner stringJoiner = new StringJoiner("_");

		for (Integer i = 0; i < columnCount; i++) {
			String columnName = request.getParameter("columns[" + i.toString() + "][name]");
			if (columnName.trim().isEmpty()) {
				columnName = request.getParameter("columns[" + i.toString() + "][data]");
			}
			String columnFilter = request.getParameter("columns[" + i.toString() + "][search][value]");
			if (columnFilter.replace("^$", "").trim().isEmpty()) {
				continue;
			}
			
			stringJoiner.add(columnName + "_" + columnFilter);
			

		}
		
		return stringJoiner.toString();
	}
	
	public static String  hash(String dto,String extra,HttpServletRequest request,HttpSession httpSession)  {
		if(httpSession != null){
			httpSession.setAttribute("search", dto);	
		}
		
		try {
			if (instance == null){
				try {
					instance = java.security.MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					//Nunca deberia Pasar...
					e.printStackTrace();
				}	
			}
			String convertNode = convertNode(dto);			
			String string = new String(instance.digest((convertNode + extra + createCriteriaFromQueryStringColumns(request)) .getBytes()));
			
			return string;
		} catch (Exception e) {
			e.printStackTrace();
			return UUID.randomUUID().toString();
		}
	}
	
	
}
