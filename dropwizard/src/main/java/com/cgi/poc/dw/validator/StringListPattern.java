package com.cgi.poc.dw.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Iterator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { StringListPattern.Validator.class })
@Documented
public @interface StringListPattern {
  String regexp();
  String message() default "Some items aren't valid";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  public class Validator implements ConstraintValidator<StringListPattern, Iterable<String>> {
    String regexp;

    @Override
    public void initialize(StringListPattern c) {
      this.regexp = c.regexp();
    }

    @Override
    public boolean isValid(Iterable<String> items, ConstraintValidatorContext ctx) {
      if (items != null) {
        Iterator<String> iterator = items.iterator();

        while (iterator.hasNext()) {
          String next = iterator.next();

          if (!next.matches(this.regexp)) {
            return false;
          }
        }
      }

      return true;//Or throw exception when list is null
    }
  }
}
