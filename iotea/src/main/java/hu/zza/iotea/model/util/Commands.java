package hu.zza.iotea.model.util;

import hu.zza.iotea.model.Command;
import java.util.MissingFormatArgumentException;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Commands {
  Pattern F_ARGUMENT = Pattern.compile("(?i)%[^%]*?[bhscdowefg]");
  Logger logger = LoggerFactory.getLogger(Command.class);

  static String build(String template, Object[] args) {
    var fArgsCount = F_ARGUMENT.matcher(template).results().count();
    if (fArgsCount != args.length) {
      logger.warn(
          "Detected arguments count ({}) mismatches with passed Object[] args length: {}",
          fArgsCount,
          args.length);
    }

    try {
      return template.formatted(args);
    } catch (MissingFormatArgumentException e) {
      logger.warn(
          "Cannot format template with given args. Returns with \"\" (empty String) as null-safe placeholder",
          e);
      return "";
    }
  }
}
