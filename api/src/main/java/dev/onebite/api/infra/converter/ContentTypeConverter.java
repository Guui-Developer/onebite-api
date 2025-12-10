package dev.onebite.api.infra.converter;

import dev.onebite.api.infra.enums.ContentType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ContentTypeConverter implements AttributeConverter<ContentType, String> {

    @Override
    public String convertToDatabaseColumn(ContentType contentType) {
        if (contentType == null) {
            return null;
        }
        return contentType.name().toLowerCase();
    }

    @Override
    public ContentType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(ContentType.values())
            .filter(c -> c.name().equalsIgnoreCase(dbData))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
