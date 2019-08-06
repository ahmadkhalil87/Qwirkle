package de.upb.cs.swtpra_03.qwirkle.controller;

public interface Controller {

    public void handleNotAllowed(String message);
    public void handleParsingError(String message);
    public void handleAccessDenied(String message);
}
