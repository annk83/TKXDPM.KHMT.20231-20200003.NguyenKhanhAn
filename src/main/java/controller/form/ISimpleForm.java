package controller.form;

import controller.utils.ChangeListener;

import java.util.List;

public interface ISimpleForm extends ChangeListener {
    boolean isValid();
    String getTitle();
    List<IFormField> getFields();
    <T> T toStringModel(Class<T> clazz);
}
