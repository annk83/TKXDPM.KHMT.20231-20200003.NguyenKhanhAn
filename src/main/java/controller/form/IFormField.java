package controller.form;

import java.util.Optional;
import java.util.function.Consumer;

public interface IFormField {
    String getName();
    String getCurrentValue();
    Optional<String> getError();
    void setConsumer(Consumer<String> errorConsumer);
    FieldType getFieldType();
}
