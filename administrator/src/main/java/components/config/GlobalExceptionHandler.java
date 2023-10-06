package components.config;

import components.services.MyBlockedException;
import components.services.RoomIsNotEmptyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResultException.class)
    public ModelAndView handleNoResultExceptionA(NoResultException ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "the instance(-s) not present!");
        return modelAndView;
    }

    @ExceptionHandler({NumberFormatException.class, DateTimeParseException.class,
            ClassCastException.class})
    public ModelAndView handleInvalidDataException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "Inputted invalid data!");
        return modelAndView;
    }

    @ExceptionHandler({NullPointerException.class, MissingServletRequestParameterException.class})
    public ModelAndView handleNoResultExceptionB(NullPointerException ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "bad gateway!");
        return modelAndView;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "the instance should have unique values!");
        return modelAndView;
    }

    @ExceptionHandler(MyBlockedException.class)
    public ModelAndView handleBlockedException(MyBlockedException ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "you've been blocked!");
        return modelAndView;
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "something goes wrong while creating a receipt!");
        return modelAndView;
    }

    @ExceptionHandler(RoomIsNotEmptyException.class)
    public ModelAndView handleIOException(RoomIsNotEmptyException ex) {
        ModelAndView modelAndView = new ModelAndView("/hotel/general/error");
        modelAndView.addObject("message", "chosen room is not empty on chosen dates!");
        return modelAndView;
    }

}
