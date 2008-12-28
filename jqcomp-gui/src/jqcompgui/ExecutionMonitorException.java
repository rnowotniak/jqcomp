/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

/**
 *
 * @author Andrzej
 */
public class ExecutionMonitorException extends RuntimeException {
    public ExecutionMonitorException(String message) {
        super(message);
    }

    public ExecutionMonitorException(Throwable cause) {
        super(cause);
    }

    public ExecutionMonitorException(String message, Throwable cause) {
        super(message, cause);
    }
}
