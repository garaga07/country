package guru.qa.country.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {
  private final String format;

  public DateSerializer(final String format) {this.format = format;}

  public DateSerializer() {this.format = null;}

  @Override
  public void serialize(
      final Date value, final JsonGenerator jgen, final SerializerProvider provider)
      throws IOException {
    jgen.writeString(new SimpleDateFormat(format).format(value));
  }
}
