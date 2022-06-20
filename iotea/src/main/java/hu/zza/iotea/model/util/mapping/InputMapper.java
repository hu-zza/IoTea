package hu.zza.iotea.model.util.mapping;

import java.util.List;

public interface InputMapper<E, I> {
  E toEntity(I inputDTO);

  List<E> toEntity(List<I> inputDTOs);
}
