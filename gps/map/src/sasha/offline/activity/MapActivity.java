package sasha.offline.activity;

import java.io.IOException;
import java.util.ArrayList;

import sasha.offline.info.Node;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
 Menu with buttons/ xml files  were created by Krisztian Litavszky
 Functionality of buttons (everything in OnClick methods)  were set by Aliaksandr Karasiou
 Methods "calldialog1" , "calldialog2" were created Aliaksandr Karasiou
 */

public class MapActivity extends Activity {

	private Drawing d; // create an object of class Drawing
	private Nav e; // create an object of class Nav

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		try {
			e = new Nav();
			e.savebounds(this); // setting bounds of a map
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			d = new Drawing(this);
			setContentView(d); // setting custom view
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (menuDialog == null) {
				menuDialog = new MenuDialog(this);
			}
			menuDialog.show();
			return true;
		}
		return false;

	}

	MenuDialog menuDialog;

	private class MenuDialog extends AlertDialog {

		Button Offpopup1, Offpopup2, Offpopup3, Offpopup4, Offpopup5;

		public MenuDialog(Context context) {
			super(context);

			View menu = getLayoutInflater().inflate(R.layout.menu, null);
			setView(menu);

			Offpopup1 = (Button) menu.findViewById(R.id.offb1);
			Offpopup2 = (Button) menu.findViewById(R.id.offb2);
			Offpopup3 = (Button) menu.findViewById(R.id.offb3);
			Offpopup4 = (Button) menu.findViewById(R.id.offb4);
			Offpopup5 = (Button) menu.findViewById(R.id.offb5);

			Offpopup1.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					d.menuACclicked = false;
					d.menuABclicked = true;

					Toast.makeText(getApplicationContext(),
							"Hold the screen to mark points",
							Toast.LENGTH_SHORT).show();

					/*
					 * clearing all variables which are used for setting path
					 */
					d.path = new ArrayList<Node>();
					d.t1x = 0;
					d.t1y = 0;
					d.t2x = 0;
					d.t2y = 0;
					d.settingpoints = 0;
					dismiss();
				}
			});

			Offpopup2.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					// TODO Auto-generated method stub
					if (d.latitude == 0 && d.longtitude == 0) {
						Toast.makeText(getApplicationContext(),
								"No GPS connection", Toast.LENGTH_SHORT).show();

					} else {
						d.menuABclicked = false;
						d.menuACclicked = true;

						Toast.makeText(getApplicationContext(),
								"Hold the screen to mark destination",
								Toast.LENGTH_SHORT).show();

						/*
						 * clearing all variables which are used for setting
						 * path
						 */

						d.path = new ArrayList<Node>();
						d.t1x = 0;
						d.t1y = 0;
						d.t2x = 0;
						d.t2y = 0;
						d.settingpoints = 0;
						System.out.println("OFFPOPUP2");
						// TODO Auto-generated method stub
					}
					dismiss();
				}

			});

			Offpopup3.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					dismiss();
				}

			});
 
			// showing current position coordinates
			
			Offpopup4.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (d.latitude == 0 && d.longtitude == 0) {
						Toast.makeText(MapActivity.this, "No GPS connection",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(
								MapActivity.this,
								"Your current position is: \n Latitude: "
										+ d.latitude + "\n Longitute: "
										+ d.longtitude, Toast.LENGTH_SHORT)
								.show();
					}

					dismiss();
				}

			});

			// clearing all the info for navigation
			
			Offpopup5.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					d.menuABclicked = false;
					d.menuACclicked = false;

					d.path = new ArrayList<Node>();
					d.t1x = 0;
					d.t1y = 0;
					d.t2x = 0;
					d.t2y = 0;
					d.settingpoints = 0;
					dismiss();
				}

			});

		}

	}

	// showing alertdialog
	public void calldialog1() {
		new AlertDialog.Builder(this)
				.setMessage("Do you want to put a point here?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									
									// calling a method
									
									d.putpointAB();
									
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Toast.makeText(MapActivity.this,
										"Point is marked", Toast.LENGTH_SHORT)
										.show();
							}
						}).setNegativeButton(android.R.string.no, null).show();

	}

	// showing alertdialog
	public void calldialog2() {
		new AlertDialog.Builder(this)

				.setMessage("Do you want to go here?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								try {
									
									// calling a method
									
									d.putpointAC();
									
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Toast.makeText(MapActivity.this,
										"Point is marked", Toast.LENGTH_SHORT)
										.show();
							}
						}).setNegativeButton(android.R.string.no, null).show();

	}
}
