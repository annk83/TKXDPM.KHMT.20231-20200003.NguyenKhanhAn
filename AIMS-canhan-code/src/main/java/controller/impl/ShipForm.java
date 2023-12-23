package controller.impl;

import controller.form.*;
import domain.IOrderDomain;
import domain.IProvinceDomain;
import lombok.AccessLevel;
import lombok.Getter;
import repo.IProvinceRepo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter(AccessLevel.MODULE)
public class ShipForm implements ISimpleForm {
    private final IProvinceRepo provinceRepo;
    private final IOrderDomain domain;
    private final List<IFormField> normalFields;
    private final List<IFormField> rushFields;
    private final IdMapped<Runnable> runnableIdMapped = new IdMapped<>();
    private final domain.ShipForm binding;

    private static String validatePhone(String str) {
        var ret = validateString(str);
        if(ret != null) return ret;
        //TODO: Validate phone number
        return null;
    }

    private static String validateString(String str) {
        if(str == null || str.isBlank())
            return "Must not leave blank";
        return null;
    }

    public ShipForm(IProvinceRepo iProvinceRepo, IOrderDomain iOrderDomain) {
        this.domain = iOrderDomain;
        this.provinceRepo = iProvinceRepo;
        binding = domain.getShipForm();
        try {
            normalFields = List.of(
                    new StringFormField("Fullname", "name", false, ShipForm::validateString),
                    new StringFormField("Address", "address", false, ShipForm::validateString),
                    new ProvinceFormField("Province", "province"),
                    new StringFormField("Phone", "phone", false, ShipForm::validateString),
                    new StringFormField("Note for shipper", "note", true, s->null)
            );
            rushFields = List.of(
                    new StringFormField("Rush Order Address", "rushAddress", false, ShipForm::validateString),
                    new ProvinceFormField("Rush Province", "rushProvince"),
                    new StringFormField("Rush Order Note", "rushNote", false, s->null)
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean isValid() {
        return getFields().stream().noneMatch(i->i.getError().isPresent());
    }

    @Override
    public String getTitle() {
        return domain.isRushing() ? "Rush Shipping Form" : "Shipping Form";
    }

    @Override
    public List<IFormField> getFields() {
        List<IFormField> v;
        if(domain.isRushing()) v = Stream.concat(normalFields.stream(), rushFields.stream()).toList();
        else v = normalFields;
        return v;
    }

    @Override
    public <T> T toStringModel(Class<T> clazz) {
        try {
            T ret = clazz.getConstructor().newInstance();
            Class<domain.ShipForm> clz = domain.ShipForm.class;
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    var ff = clz.getDeclaredField(f.getName());
                    ff.setAccessible(true);
                    var obj = ff.get(binding);
                    if(obj instanceof IProvinceDomain p)
                    f.set(ret, p.getName());
                    else f.set(ret, obj == null ? "" : obj.toString());
                } catch (NoSuchFieldError ignore) {
                    f.set(ret, "");
                }
            }
            return ret;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int addChangeListener(Runnable runnable) {
        return runnableIdMapped.addObj(runnable);
    }

    @Override
    public void removeChangeListener(int runnable) {
        runnableIdMapped.removeByKey(runnable);
    }

    private class ProvinceFormField implements ISelectFormField {
        @Getter
        private final String name;
        private final Field field;
        private List<SelectOption> options;
        private List<IProvinceDomain> provinceDomains;
        @Getter
        private SelectOption currentSelection;

        private ProvinceFormField(String name, String fieldName) throws NoSuchFieldException {
            this.name = name;
            field = binding.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            try {
                var obj = (IProvinceDomain)field.get(binding);
                provinceDomains = provinceRepo.getAllProvince();
                options = provinceDomains.stream().map(i->new SelectOption(i.getProvinceId(), i.getName())).toList();
                if(obj != null)
                    for (SelectOption option : options)
                        if (option.getId() == obj.getProvinceId()) {
                            currentSelection = option;
                            break;
                        }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Optional<String> getError() {
            if(currentSelection == null) return Optional.of("Must choose");
            else return Optional.empty();
        }

        private Consumer<String> consumer;
        @Override
        public void setConsumer(Consumer<String> errorConsumer) {
            consumer = errorConsumer;
        }

        @Override
        public List<SelectOption> getOptionList() {
            provinceDomains = provinceRepo.getAllProvince();
            options = provinceDomains.stream().map(i->new SelectOption(i.getProvinceId(), i.getName())).toList();
            return options;
        }

        @Override
        public void select(int id) throws ArrayIndexOutOfBoundsException {
            currentSelection = options.get(id);
            try {
                field.set(binding, provinceDomains.get(id));
                binding.setProvince(provinceDomains.get(id));
                if(consumer!=null) consumer.accept(null);
                runnableIdMapped.foreach(Runnable::run);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            runnableIdMapped.foreach(Runnable::run);
        }

        @Override
        public void backToDefault() {
            currentSelection = null;
        }
    }
    private class StringFormField implements ITextFormField {
        @Getter
        private final String name;
        @Getter
        private String currentValue;
        private String error;
        @Getter
        private final boolean paragraph;
        private final Field field;
        private final Function<String, String> validator;

        private StringFormField(String name, String fieldName, boolean paragraph, Function<String, String> validator) throws NoSuchFieldException {
            this.name = name;
            this.paragraph = paragraph;
            this.validator = validator;
            field = binding.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            try {
                currentValue = (String)field.get(binding);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            error = validator.apply(currentValue);
        }

        @Override
        public Optional<String> getError() {
            if(error == null) return Optional.empty();
            return Optional.of(error);
        }

        private Consumer<String> consumer;
        @Override
        public void setConsumer(Consumer<String> errorConsumer) {
            consumer = errorConsumer;
        }

        @Override
        public void setValue(String value) {
            try {
                currentValue = value;
                error = validator.apply(currentValue);
                if(consumer != null) consumer.accept(error);
                field.set(binding, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            runnableIdMapped.foreach(Runnable::run);
        }
    }
}
