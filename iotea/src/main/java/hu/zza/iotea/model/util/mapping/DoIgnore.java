package hu.zza.iotea.model.util.mapping;

import java.lang.annotation.*;
import org.mapstruct.Qualifier;

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface DoIgnore {}
