package controller.form;

public interface ITextFormField extends IFormField {
    void setValue(String value);
    boolean isParagraph();
    default FieldType getFieldType() { return FieldType.TEXT; }
}
