package org.mkgroup.zaga.workorderservice.odata;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ODataToDTOConvertor {
	
	@Autowired
	ModelMapper mapper;
	
	public Object convertODataSetToDTO(String odata) throws JSONException {
		
		JSONObject jsonFromOData = new JSONObject(odata);
		JSONObject dObject = new JSONObject(jsonFromOData.get("d").toString());
		JSONArray jsonResultsArray = new JSONArray(dObject.get("results").toString());
		ArrayList<Object> dtoList = new ArrayList<Object>();
		for(int i = 0; i < jsonResultsArray.length(); i++) {
			JSONObject singleResult = (JSONObject) jsonResultsArray.getJSONObject(i);
			JSONObject parsedJSONObject = new JSONObject();
			Iterator<String> iterator = singleResult.keys();
		      while(iterator.hasNext()) {
		        String currentKey = iterator.next();
		        if(!currentKey.equals("__metadata")) { //skip this one
		        	parsedJSONObject.put(currentKey, singleResult.get(currentKey));
		        }
		      }
			
			dtoList.add(parsedJSONObject);
		}
		return dtoList;
	}

}
