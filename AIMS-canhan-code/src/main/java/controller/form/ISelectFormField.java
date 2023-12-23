package controller.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public interface ISelectFormField extends IFormField {
    List<SelectOption> getOptionList();
    void select(int id) throws ArrayIndexOutOfBoundsException;
    SelectOption getCurrentSelection();
    void backToDefault();
    default FieldType getFieldType() { return FieldType.SELECT; }
    default String getCurrentValue() { var i = getCurrentSelection(); return i == null ? null : i.getLabel(); }

    @Data
    @AllArgsConstructor
    class SelectOption {
        private final int id;
        private final String label;
        @Override
        public String toString() {
           return label;
        }
    }
}
