package com.lozko;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;

public class PasswordService {

  private static final RandomStringGenerator generator = new RandomStringGenerator.Builder()
      .withinRange('a', 'z')
      .build();

  @Getter
  private String password = generator.generate(3);

  @Getter
  private final Predicate<String> passwordCheckerString = s -> password.equals(s);

  public void generate() {
    password = generator.generate(3);
  }

}
