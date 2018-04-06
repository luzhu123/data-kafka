package com.keruyun.fintech.commons.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.keruyun.fintech.commons.Constant;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/2/7 17:08
 */
public class TimestampSerializer extends JsonSerializer<Timestamp> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    static {
        dateFormat.setTimeZone(Constant.TIME_ZONE);
    }
    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (null == timestamp) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeString(dateFormat.format(timestamp));
    }
}
