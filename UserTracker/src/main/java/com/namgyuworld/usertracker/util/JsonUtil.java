package com.namgyuworld.usertracker.util;

import com.namgyuworld.usertracker.model.TrackingModel;

import java.util.Iterator;
import java.util.List;

/**
 * Convert List Object to Json or vice versa
 * <br><br.
 * Created by danielpark on 6/16/15.
 */
public class JsonUtil {

    /**
     * Convert List to Json
     * @param list
     * @return
     */
    public String toJson(List<TrackingModel> list){
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(int i=0; i < list.size(); i++){
            sb.append("\"" + list.get(i).getKey() + "\"");
            sb.append(":");

//            if(list.get(i).getValue() instanceof String){
                sb.append("\"" + list.get(i).getValue() + "\"");
//            }
            sb.append(",");
        }

        sb.delete(sb.lastIndexOf(","), sb.length() );

        sb.append("}");

        return sb.toString();
    }
}
