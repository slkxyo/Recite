package org.slkxy.recite.entity.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public class JsonConverter<T>  implements AttributeConverter<T,String> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<T> typeRef;

    public JsonConverter(TypeReference<T> typeReference) {
        this.typeRef = typeReference;
    }

    @Override
    public String convertToDatabaseColumn(T t) {
        if(t==null)  return null;
        try{
            return mapper.writeValueAsString(t);
        }
        catch(Exception e){
           throw new RuntimeException(e);
        }
    }

    @Override
    public T convertToEntityAttribute(String s) {
        if(s==null)  return null;
        try{
            return mapper.readValue(s, typeRef);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
