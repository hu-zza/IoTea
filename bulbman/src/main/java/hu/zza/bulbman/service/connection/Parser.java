package hu.zza.bulbman.service.connection;

import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface Parser<T> {
  JSONObject parse(String raw);
}
