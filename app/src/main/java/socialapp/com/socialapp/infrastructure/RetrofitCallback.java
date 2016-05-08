package socialapp.com.socialapp.infrastructure;

import android.util.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by SAMAR on 5/3/2016.
 */
public abstract class RetrofitCallback<T extends ServiceResponse> implements Callback<T> {

    private static String TAG = "RetrofitCallback";

    protected final Class<T> resultType;

    public RetrofitCallback(Class<T> resultType) {
        this.resultType = resultType;
    }

    protected abstract void onResponse(T t) ;
    @Override
    public void success(T t, Response response) {
        onResponse(t);
    }


    @SuppressWarnings("unchecked")
    @Override
    public void failure(RetrofitError error) {
        Log.e("Error w " + resultType.getName() + " response", error.toString());

        ServiceResponse errorResult;

        try {
            errorResult = resultType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error creating result type " + resultType.getName(), e);
        }

        if (error.getKind() == RetrofitError.Kind.NETWORK) {
            errorResult.setCriticalError("Unable to conenct to yora service!");
            onResponse((T) errorResult);
            return;
        }

        if (error.getSuccessType() == null) {
            errorResult.setCriticalError("Unknown error, Please try again");
            onResponse((T) errorResult);
        }

        try {
            if (error.getBody() instanceof ServiceResponse){
                ServiceResponse result = (ServiceResponse) error.getBody();
                if (result.didSucceed()) {
                    result.setCriticalError("Unknown error. Please try again");
                }

                onResponse((T) result);
            } else {
                throw new RuntimeException("Result class " + resultType.getName() + " does not extend ServiceResponse");
            }
        } catch (Exception e) {
            Log.e(TAG, "Unknown error", e);
            errorResult.setCriticalError("Unknown error, Please try again");
            onResponse((T)errorResult);
        }
    }
}
