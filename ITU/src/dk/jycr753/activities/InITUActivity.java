package dk.jycr753.activities;

import java.io.IOException;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import dk.jycr753.bluetooth.BluetoothListener;
import dk.jycr753.bluetooth.GetDeviceBluetoothInfo;
import dk.jycr753.bluetooth.PossibleBluetoothDevices;
import dk.jycr753.itu.R;
import dk.jycr753.location.GetCurrentZone;

public class InITUActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                .permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	    }

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itu_layout);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.gettingContentProgressBar);
		progressBar.setVisibility(View.GONE);
		TextView readytext = (TextView)this.findViewById(R.id.itu_text_view_title);
		String macAddress = GetDeviceBluetoothInfo.getDeviceMacAddress();
		String finalMacAddressNoColums = GetDeviceBluetoothInfo.removeColumnsFromMacAddress(macAddress);
		if(!GetDeviceBluetoothInfo.isBluetoothEnable()){
			Intent enableBtIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
		    startActivityForResult(enableBtIntent, 0);
		}
		if(finalMacAddressNoColums != null){
		
			readytext.setText(finalMacAddressNoColums);
			//from here Activate Listener to any available BLIP
			if(BluetoothListener.isThereAnyConnectionAlive()){
				progressBar.setVisibility(View.VISIBLE);
				//so if there is any connection...
				//get current location of device
				String deviceCurrentZone = GetCurrentZone.getDeviceCurrentZone(finalMacAddressNoColums);
				readytext.setText(deviceCurrentZone);
				
				
				/***************/
				//temporal work to test devices.....
				String testMacAddress = "00:a0:96:09:1c:36";
				boolean testIfProvidedMacAddressIsValid;
				try {
					testIfProvidedMacAddressIsValid = PossibleBluetoothDevices.isDeviceLegalToConnect(testMacAddress);
					if(testIfProvidedMacAddressIsValid){
						
						readytext.setText("true --- ");
						progressBar.setVisibility(View.GONE);
					
					}else{
						progressBar.setVisibility(View.GONE);
						readytext.setText("false");
					
					}
				
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			}else{
				//something wrong in the connection.... try again!
				readytext.setText("No Connected 2 bluetooth");
			}
		
		}else{
			
			//Handle All Errors, in Case this methods fails...
			readytext.setText("No Data");
		}
		
	}
	
}
