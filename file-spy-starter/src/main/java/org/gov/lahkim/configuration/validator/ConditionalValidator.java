package org.gov.lahkim.configuration.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
/**
 * @author Ayoub LAHKIM
 */

@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {

    @Override
    public void initialize(Conditional requiredIfChecked) {
    }

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        Boolean valid = true;

        try {
            Object watcherValue = BeanUtils.getProperty(objectToValidate, "watcher");
            Object cronValue = BeanUtils.getProperty(objectToValidate, "cron");

            if ((watcherValue == null && cronValue == null) || (watcherValue != null && cronValue != null)) {
                valid = false;
            }
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Only one of these fields must be not null : cron | watcher").addConstraintViolation();
            }
        } catch (IllegalAccessException e) {
            log.error("Accessor method is not available for class : {}, exception : {}", objectToValidate.getClass().getName(), e);
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            log.error("Field or method is not present on class : {}, exception : {}", objectToValidate.getClass().getName(), e);
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            log.error("An exception occurred while accessing class : {}, exception : {}", objectToValidate.getClass().getName(), e);
            e.printStackTrace();
            return false;
        }
        return valid;
    }
}
