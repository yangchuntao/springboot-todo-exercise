package com.tdwl.wife.sql.form.converter;


import com.tdwl.wife.sql.form.AbstractForm;
import com.tdwl.wife.sql.po.AbstractEntity;
import org.springframework.stereotype.Component;

@Component
public interface AbstractFormConverter<E extends AbstractEntity, F extends AbstractForm> {

    F fromEntity(E e) throws Exception;

    E toEntity(F f) throws Exception;

}
