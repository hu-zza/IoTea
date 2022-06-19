package hu.zza.iotea.model.util;

import hu.zza.iotea.model.Command;
import java.util.MissingFormatArgumentException;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Commands {
  Pattern F_ARGUMENT = Pattern.compile("(?i)%[^%]*?[bhscdowefg]");
  Logger logger = LoggerFactory.getLogger(Command.class);

  static String build(String commandTemplate, Object[] args) {
    var template = sanitizeTemplate(commandTemplate);
    var arguments = sanitizeArgs(args);

    var fArgsCount = F_ARGUMENT.matcher(template).results().count();
    warnIfParameterCountMismatches(arguments, fArgsCount);

    return tryToFormatTemplate(template, arguments);
  }

  private static String sanitizeTemplate(String template) {
    if (template == null) {
      logger.warn(
          "'commandTemplate' cannot be null. Returns with \"\" (empty String) as null-safe placeholder");
      return "";
    }
    return template;
  }

  private static Object[] sanitizeArgs(Object[] args) {
    if (args == null) {
      logger.warn("'args' cannot be null. Returns with 'new Object[0]' as null-safe placeholder");
      return new Object[0];
    }
    return args;
  }

  private static void warnIfParameterCountMismatches(Object[] args, long fArgsCount) {
    if (fArgsCount != args.length) {
      logger.warn(
          "Detected arguments count ({}) mismatches with passed Object[] args length: {}",
          fArgsCount,
          args.length);
    }
  }

  private static String tryToFormatTemplate(String template, Object[] args) {
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
