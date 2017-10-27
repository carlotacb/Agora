package edu.upc.pes.agora;

/**
 * This activity was created only in test purposes.
 * Is can accept a callback by {@link #setLoginCallback(LoginTestCallback)}
 * The {@link LoginTestCallback#onHandleResponseCalled(LoginResponse)} will be called when login response arrives.
 *
 * Using this approach the calling side can validate that login response received.
 */
public class LoginTestActivity extends LoginActivity {

    private LoginTestCallback mCallback;

    public void setLoginCallback(LoginTestCallback loginCallback){
        mCallback = loginCallback;
    }

    public interface LoginTestCallback{
        void onHandleResponseCalled(LoginResponse loginResponse);
    }

    @Override
    public void handleLoginResponse(LoginResponse loginResponse) {
        mCallback.onHandleResponseCalled(loginResponse);
    }
}