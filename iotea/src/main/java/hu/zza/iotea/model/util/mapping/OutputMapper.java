package hu.zza.iotea.model.util.mapping;

import java.util.List;

public interface OutputMapper<E, O> {
  O toDto(E entity);

  List<O> toDto(List<E> entities);
}
