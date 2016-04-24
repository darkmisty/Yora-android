package socialapp.com.socialapp.infrastructure;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.TreeMap;

import socialapp.com.socialapp.Utilities.utils;

public class ServiceResponse {

    private static final String TAG = "ServiceResponse";

    private String operationError;
    private HashMap<String, String> propertyError;
    private boolean isCritical;
    private TreeMap<String, String> propertyErrorCaseInSensitive;


    public ServiceResponse() {
        propertyError = new HashMap<>();
    }

    public ServiceResponse(String operationError) {
        this.operationError = operationError;
    }

    public ServiceResponse(String operationError, boolean isCritical) {
        this.operationError = operationError;
        this.isCritical = isCritical;
    }

    public String getOperationError() {
        return operationError;
    }

    public void setOperationError(String operationError) {
        this.operationError = operationError;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    public void setCriticalError(String criticalError) {
        isCritical = true;
        operationError = criticalError;
    }


    public void setPropertyError(String property, String error) {
        propertyError.put(property, error);
    }

    public String getPropertyError(String property) {

        if (propertyErrorCaseInSensitive == null || propertyErrorCaseInSensitive.size() != propertyError.size()) {
            propertyErrorCaseInSensitive = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            propertyErrorCaseInSensitive.putAll(propertyError);
        }

        return propertyError.get(property);
    }


    public boolean didSucceed() {
        return (operationError == null || operationError.isEmpty()) && (propertyError.size() == 0);
    }

    public void showErrorToast(Context context) {

        if (context == null || operationError == null || operationError.isEmpty())
            return;

        try {
            utils.tmsg((Activity) context, operationError);
        } catch (Exception e) {
            Log.e(TAG, "Can't Create error Toast", e);
        }
    }


}
