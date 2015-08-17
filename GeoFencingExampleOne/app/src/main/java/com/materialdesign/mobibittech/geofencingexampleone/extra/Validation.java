package com.materialdesign.mobibittech.geofencingexampleone.extra;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by rajendra on 2/8/2015.
 */
public class Validation {
	static   boolean confirm ;
	public static boolean confirmPassrod(final EditText newpassord , final EditText confirmpassword){
		String newpass = newpassord.getText().toString();
		
		confirmpassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(newpassord.getText().toString().equals(confirmpassword.getText().toString())){
					confirmpassword.setError(null);
					confirm = true;
				}else{
					confirmpassword.setError("password does not match");
					confirm=false;
				}
			}
		});
		return confirm;
	}

	public final static int EMAIL=1;
	public final static int PHONE =2;
	public static boolean isValid(final EditText text , final int choice) {
		switch (choice) {
		case EMAIL:
			String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			if (text.getText().toString().matches(EMAIL_PATTERN)) {
				text.setError(null);
				return true;
			} else {
				text.setError("Invalid email");
				return false;
			}
		case PHONE:
			String MOBILE_PATTERN = "\\d{10}";
			if (text.getText().toString().matches(MOBILE_PATTERN)) {
				text.setError(null);
				return true;
			} else {
				text.setError("Invalid mobile no");
				return false;
			}
		default:
			return false;
		}
	}
	public static boolean isValidLength(EditText text ,int length,String message) {
		if (text.getText().toString().length() >= length) {
			text.setError(null);
			return true;
		} else {
			text.setError(message);
			return false;
		}
	}


    public static boolean isValidMobile(EditText txtPhone)
    {
        String phone2 = txtPhone.getText().toString();
        boolean check;
        if(phone2.length() < 6 || phone2.length() > 13)
        {
            check = false;
            txtPhone.setError("Not Valid Number");
        }
        else
        {
            check = true;
        }
        return check;
    }

}