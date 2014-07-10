package sasha.offline.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sasha.offline.info.Node;
import sasha.offline.info.Tag;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/*
 Author of the class : Aliaksandr Karasiou
 */
public class Drawing extends View implements OnClickListener {

	private PointF mid = new PointF();
	private PointF start = new PointF();
	private float oldDist;
	private final int NONE = 0; // mode for moving map
	private final int DRAG = 1; // mode for moving the map
	private final int ZOOM = 2; // mode for moving map
	private float scale; // scale value of the matrix
	private float ZoomLevel = 0; // value for tracking zoom level of the matrix

	/*
	 * variable for changing layers according to ZoomLevel
	 */

	private int LayerMode = 3;
	private float newDist;
	private int mode = NONE; // variable for setting matrix's moving mode

	/*
	 * integer for putting point A or point B for creating path according to the
	 * value of this integer
	 */
	int settingpoints = 0;
	private Matrix matrix = new Matrix(); // matrix for the map

	/*
	 * previous condition of matrix for comparing, also used for dragging and
	 * zooming events
	 */

	private Matrix savedMatrix = new Matrix();
	private GestureDetector gestureDetector; // detector for MotionEvents

	private Nav e; // Object of class Nav
	private MapActivity t; // Object of class MapActivity
	private Dijkstra algo; // Object of class Dijkstra

	/*
	 * pictures for layer 0 , 1, 2, 3, 4, 5
	 */
	private Picture pic0 = null;
	private Picture pic1 = null;
	private Picture pic2 = null;
	public Picture pic3 = null;
	public Picture pic4 = null;
	public Picture pic5 = null;

	private int loadlevel = 6; // integer for tracking loading process
	double latitude = 0; // current location latitude
	double longtitude = 0; // current location longitude

	/*
	 * Bitmaps 1, 2, 3 for pictures which show loading progress
	 */
	private Bitmap f;
	private Bitmap f2;
	private Bitmap f3;

	private float[] values; // array for Matrix's constants

	/*
	 * value which represents x coordinates for pictures which show loading
	 * progress when program starts.
	 */

	private int widght = 0;

	/*
	 * value which represents y coordinates for pictures which show loading
	 * progress when program starts.
	 */
	private int height = 0;

	/*
	 * value which represents x coordinates for pictures which show loading
	 * progress and changing while moving the matrix.
	 */
	private int picx = 0;

	/*
	 * value which represents y coordinates for pictures which show loading
	 * progress and changing while moving the matrix.
	 */

	private int picy = 0;
	private int transx = 0; // value which represents -(Matrix.MTRANS_X)
	private int transy = 0; // value which represents -(Matrix.MTRANS_Y)

	/*
	 * x coordinates for point where user touches the screen
	 */
	private float touchx = 0;

	/*
	 * y coordinates for point where user touches the screen
	 */
	private float touchy = 0;

	private Paint p; // Object of class Paint for suburbs' names
	private Paint drawpath; // Object of class Paint for drawing a path
	private Rect rect; // Object of class Rect for current position icon

	/*
	 * Object of class Rect for finding nodes for navigaation inside it
	 */
	private Rect fornav;

	private Drawable pin; // Object of class Drawable for current location icon

	/*
	 * Objects of class Drawable for icons: restaurant, cafe, pub, fastfood,
	 * supermarket, hotel, hospital, gas station
	 */

	private Drawable res;
	private Drawable caf;
	private Drawable pub;
	private Drawable fastfood;
	private Drawable shop;
	private Drawable hotel;
	private Drawable hospital;
	private Drawable gas;

	/*
	 * boolean for enabling/disable zoom mode
	 */
	private boolean EnableZoom = false;

	/*
	 * boolean which enables to set some variables once in OnDraw method (it has
	 * a command "invalidate)
	 */
	private boolean trigger = false;

	/*
	 * boolean which enables to show POI's info only after set amount of time in
	 * order to avoid big amount of Toasts
	 */
	private boolean showinfo = true;

	/*
	 * boolean which enables to show POI only for certain ZoomLevel
	 */
	private boolean Enableicons = false;
	private boolean pathcreated = false; // tracks if path is done or not

	public boolean menuABclicked = false; // tracks if menu "A-B is on or off

	/*
	 * tracks if menu "Current Location - B" is on or off
	 */
	public boolean menuACclicked = false;

	/*
	 * ArrayList of nodes for suburbs' names
	 */
	private ArrayList<Node> suburbs = null;

	/*
	 * ArrayList of nodes for each POI and ArrayList of rectangles for this POI
	 */
	private ArrayList<Node> restaurants = null;
	private ArrayList<Rect> plres = null;

	private ArrayList<Node> gases = null;
	private ArrayList<Rect> plgas = null;

	private ArrayList<Node> cafes = null;
	private ArrayList<Rect> plcaf = null;

	private ArrayList<Node> pubs = null;
	private ArrayList<Rect> plpub = null;

	private ArrayList<Node> fast = null;
	private ArrayList<Rect> plfast = null;

	private ArrayList<Node> market = null;
	private ArrayList<Rect> plmarket = null;

	private ArrayList<Node> hotels = null;
	private ArrayList<Rect> plhotels = null;

	private ArrayList<Node> hospitals = null;
	private ArrayList<Rect> plhospitals = null;

	ArrayList<Node> path = new ArrayList<Node>(); // ArrayList of nodes for path

	private Node point1 = new Node(); // Point A for navigating
	int t1x = 0; // x coordinates for Point A
	int t1y = 0; // y coordinates for Point A

	private Node point2 = new Node(); // Point B for navigating
	int t2x = 0; // x coordinates for Point B
	int t2y = 0; // y coordinates for Point B

	public Drawing(Context context) throws IOException, InterruptedException {
		super(context);
		/*
		 * setting Paint for drawing path
		 */
		drawpath = new Paint();
		drawpath.setAntiAlias(true);
		drawpath.setStrokeWidth(1);
		drawpath.setColor(Color.RED);
		Cap c = Cap.ROUND;
		drawpath.setStrokeCap(c);

		/*
		 * setting Paint for showing suburbs' names
		 */
		p = new Paint();
		p.setAntiAlias(true);
		p.setTextSize(40);

		/*
		 * declaring variables
		 */

		algo = new Dijkstra();
		rect = new Rect(0, 0, 0, 0);
		fornav = new Rect(0, 0, 0, 0);

		/*
		 * setting suburbs
		 */
		suburbs = suburbs();

		/*
		 * This steps are equal: Loading file with POI ,Setting information to
		 * memory(ArrayList of nodes), Declaring ArrayList of rectangles,
		 * Setting location of icons(Setting ArrayList of rectangles according
		 * to Arraylist of nodes)
		 */

		InputStream is = this.getResources().openRawResource(R.raw.restaurants);
		restaurants = setinfo(is);
		plres = new ArrayList<Rect>();
		iconlocation(restaurants, plres);

		is = this.getResources().openRawResource(R.raw.hotel);
		hotels = setinfo(is);
		plhotels = new ArrayList<Rect>();
		iconlocation(hotels, plhotels);

		is = this.getResources().openRawResource(R.raw.fuel);
		gases = setinfo(is);
		plgas = new ArrayList<Rect>();
		iconlocation(gases, plgas);

		is = this.getResources().openRawResource(R.raw.cafes);
		cafes = setinfo(is);
		plcaf = new ArrayList<Rect>();
		iconlocation(cafes, plcaf);

		is = this.getResources().openRawResource(R.raw.pub);
		pubs = setinfo(is);
		plpub = new ArrayList<Rect>();
		iconlocation(pubs, plpub);

		is = this.getResources().openRawResource(R.raw.fastfood);
		fast = setinfo(is);
		plfast = new ArrayList<Rect>();
		iconlocation(fast, plfast);

		is = this.getResources().openRawResource(R.raw.supermarket);
		market = setinfo(is);
		plmarket = new ArrayList<Rect>();
		iconlocation(market, plmarket);

		is = this.getResources().openRawResource(R.raw.hospital);
		hospitals = setinfo(is);
		plhospitals = new ArrayList<Rect>();
		iconlocation(hospitals, plhospitals);

		/*
		 * setting pictures which show loading progress
		 */
		f = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
		f2 = BitmapFactory.decodeResource(getResources(), R.drawable.splash2);
		f3 = BitmapFactory.decodeResource(getResources(), R.drawable.splash3);

		/*
		 * setting picture for current location
		 */
		pin = getResources().getDrawable(R.drawable.loc);

		/*
		 * setting pictures for POI
		 */
		res = getResources().getDrawable(R.drawable.restaurant);
		gas = getResources().getDrawable(R.drawable.gasstation);
		caf = getResources().getDrawable(R.drawable.cafe);
		pub = getResources().getDrawable(R.drawable.pub);
		fastfood = getResources().getDrawable(R.drawable.fast_food);
		shop = getResources().getDrawable(R.drawable.shop);
		hotel = getResources().getDrawable(R.drawable.hotel);
		hospital = getResources().getDrawable(R.drawable.hospital);

		/*
		 * setting GPS current location settings
		 */
		t = (MapActivity) context;
		LocationManager mlocManager = (LocationManager) t
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new mylocationlistener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);

		/*
		 * Thread for loading map layers. They are created by parsing
		 * information from .svg files by SVF Parser "loadlevel" is changing
		 * according to loading progress After loading all map layers it calls a
		 * method to load all information need for navigation
		 */

		new Thread(new Runnable() {

			public void run() {

				SVG svg = SVGParser.getSVGFromResource(getResources(),
						R.raw.layer0);
				pic0 = svg.getPicture();
				System.out.println(" L0 DONE");

				loadlevel = 0;

				svg = SVGParser
						.getSVGFromResource(getResources(), R.raw.layer1);
				pic1 = svg.getPicture();
				System.out.println(" L1 DONE");

				loadlevel = 1;

				svg = SVGParser
						.getSVGFromResource(getResources(), R.raw.layer2);
				pic2 = svg.getPicture();
				System.out.println("L2 DONE");

				loadlevel = 2;

				svg = SVGParser
						.getSVGFromResource(getResources(), R.raw.layer3);
				pic3 = svg.getPicture();
				System.out.println("L3 DONE");

				loadlevel = 3;

				svg = SVGParser
						.getSVGFromResource(getResources(), R.raw.layer4);
				pic4 = svg.getPicture();
				System.out.println("L4 DONE");

				loadlevel = 4;

				svg = SVGParser
						.getSVGFromResource(getResources(), R.raw.layer5);
				pic5 = svg.getPicture();

				// loading navigation information
				try {
					e = new Nav();
					e.savenav(t);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("L5 DONE");
				loadlevel = 5;

				// while everything is loading you cant zoom in/out. Here
				// boolean enables zoom
				EnableZoom = true;
			}
		}).start();

	}

	/*
	 * Location Listener and default methods for GPS tracking
	 */
	private class mylocationlistener implements LocationListener {
		public void onLocationChanged(Location location) {

			if (location != null) {

				/*
				 * setting latitude and longitude when GPS device tracks
				 * changing device location
				 */

				Log.d("LOCATION CHANGED", location.getLatitude() + "");
				Log.d("LOCATION CHANGED", location.getLongitude() + "");
				latitude = location.getLatitude();
				longtitude = location.getLongitude();
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);

		c.setMatrix(matrix);

		/*
		 * Activation GestureDetector for manipulating Matrix (map)
		 */
		gestureDetector = new GestureDetector(getContext(),
				new GestureListener());

		/*
		 * setting rect (Rectangle) coordinates for current location icon
		 * depending on current location
		 */
		rect.set((int) ((latitude - e.minx) * e.x - (int) 16 / ZoomLevel),
				(int) ((longtitude - e.miny) * e.y - (int) 32 / ZoomLevel),
				(int) ((latitude - e.minx) * e.x + (int) 16 / ZoomLevel),
				(int) ((longtitude - e.miny) * e.y));

		/*
		 * setting variables for pictures(loading progress) and doing it only
		 * once because of the boolean "trigger"(OnDraw method is invalidate)
		 */

		if (trigger == false) {
			widght = this.getWidth() / 2 - 200;
			height = this.getHeight() / 2 - 70;
			picx = widght;
			picy = height;
			trigger = true;
		}

		/*
		 * depending on load level it will show certain layers( already loaded).
		 * Also showing loading progress pictures
		 */
		if (loadlevel != 5) {

			if (loadlevel == 0) {
				c.drawPicture(pic0);
			}

			if (loadlevel == 1) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawBitmap(f, picx, picy, new Paint());
			}

			if (loadlevel == 2) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawBitmap(f, picx, picy, new Paint());
			}

			if (loadlevel == 3) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawBitmap(f2, picx, picy, new Paint());
			}

			if (loadlevel == 4) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawPicture(pic3);
				c.drawPicture(pic4);
				c.drawBitmap(f3, picx, picy, new Paint());
			}

		}
		/*
		 * After loading is complete and load level is set 5,it will information
		 * depended on layer mode LayerMode depend on ZoomLevel of Matrix, which
		 * is changing while zooming in/out
		 */

		else {
			if (LayerMode == 1) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawPicture(pic5);

				/* for loop for showing suburbs' names */

				for (int i = 0; i < suburbs.size(); i++) {
					c.drawCircle((float) (suburbs.get(i).getlat() - e.minx)
							* e.x, (float) (suburbs.get(i).getlon() - e.miny)
							* e.y, 4, new Paint());
					c.drawText(suburbs.get(i).getname(), (float) (suburbs
							.get(i).getlat() - e.minx) * e.x, (float) (suburbs
							.get(i).getlon() - e.miny) * e.y - 5, p);
				}
			}

			if (LayerMode == 2) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawPicture(pic4);
				c.drawPicture(pic5);

			}
			if (LayerMode == 3) {
				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawPicture(pic3);
				c.drawPicture(pic4);
				c.drawPicture(pic5);

			}

			/*
			 * when Layer mode is maximum, it's drawing icons for POI (calling
			 * method for it)
			 */

			if (LayerMode == 4) {

				c.drawPicture(pic0);
				c.drawPicture(pic1);
				c.drawPicture(pic2);
				c.drawPicture(pic3);
				c.drawPicture(pic4);
				c.drawPicture(pic5);

				drawicons(c, plgas, gas);
				drawicons(c, plres, res);
				drawicons(c, plcaf, caf);
				drawicons(c, plpub, pub);
				drawicons(c, plfast, fastfood);
				drawicons(c, plmarket, shop);
				drawicons(c, plhotels, hotel);
				drawicons(c, plhospitals, hospital);
			}

		}

		/*
		 * if coordinates of Point A,B are not zero (they are set) it will draw
		 * it
		 */

		if (t1x != 0 && t1y != 0) {
			c.drawCircle(t1x, t1y, 3, drawpath);
			c.drawText("Point A", t1x, t1y - 4, new Paint());
		}

		if (t2x != 0 && t1y != 0) {
			c.drawCircle(t2x, t2y, 3, drawpath);
			c.drawText("Point B", t2x, t2y - 4, new Paint());
		}

		/*
		 * if path is set it will draw it
		 */
		if (pathcreated == true) {
			for (int i = 0; i < path.size() - 1; i++) {
				c.drawLine((float) (path.get(i).getlat() - e.minx) * e.x,
						(float) (path.get(i).getlon() - e.miny) * e.y,
						(float) (path.get(i + 1).getlat() - e.minx) * e.x,
						(float) (path.get(i + 1).getlon() - e.miny) * e.y,
						drawpath);
			}
		}

		/*
		 * setting coordinates of current location icon and drawing it
		 */

		pin.setBounds(rect);
		pin.draw(c);

		/*
		 * invalidate function is used for updating current location all the
		 * time
		 */
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:

			/*
			 * setting coordinates of loading progress pictures depending on
			 * certain variables
			 */
			picx = widght + transx;
			picy = height + transy;

			/*
			 * changing mode to enable drag the MAtrix
			 */
			mode = DRAG;

			/*
			 * saving previous condition of Matrix in case of getting previous
			 * settings
			 */
			savedMatrix.set(matrix);

			/*
			 * setting coordinates of a point on the screen where user touches
			 * it
			 */

			start.set(event.getX(), event.getY());

			/*
			 * saving x,y coordinates separately
			 */

			touchx = event.getX();
			touchy = event.getY();

			/*
			 * calling methods for setting navigation information / starting
			 * path algorithm if menu was activated
			 */
			if (menuABclicked == true) {
				t.calldialog1();
			}

			if (menuACclicked == true) {
				t.calldialog2();
			}

			/*
			 * this boolean is used for disabling checking POI when there is
			 * inappropriate LayerMode
			 */
			if (Enableicons == true) {

				/*
				 * calling method to check if user touched the icon (coordinates
				 * of touched point is in rectangle or not)
				 */
				checkpoi(plres, restaurants, "Restaurant: ");
				checkpoi(plcaf, cafes, "Cafe: ");
				checkpoi(plpub, pubs, "Pub: ");
				checkpoi(plfast, fast, "Fast food: ");
				checkpoi(plmarket, market, "Supermarket: ");
				checkpoi(plhotels, hotels, "Hotel: ");
				checkpoi(plhospitals, hospitals, "Hospital: ");
				checkpoi(plgas, gases, "Gas station: ");
			}
			break;

		case MotionEvent.ACTION_UP:
			/*
			 * disabling all the modes
			 */
			mode = NONE;

			/*
			 * getting coordinates for "loading" pictures
			 */
			picx = widght + transx;
			picy = height + transy;

			/*
			 * Setting zoom level boundaries for map
			 */

			if (ZoomLevel < 0.28) {
				matrix.setScale((float) 0.28, (float) 0.28);
			}

			if (ZoomLevel > 22) {
				matrix.set(savedMatrix);
			}

			invalidate();
			break;

		case MotionEvent.ACTION_POINTER_UP:

			mode = NONE;
			invalidate();
			break;

		case MotionEvent.ACTION_POINTER_DOWN:

			oldDist = spacing(event);

			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);

				if (EnableZoom == true) {
					mode = ZOOM; // setting mode for zooming
				}
				invalidate();
			}
			break;

		case MotionEvent.ACTION_MOVE:

			if (mode == DRAG) {

				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y); // changing position of the Matrix

				picx = widght + transx; // setting coordinates for "loading"
										// pictures
				picy = height + transy;

				invalidate();
			} else if (mode == ZOOM) {

				newDist = spacing(event);

				if (newDist > 10f) {
					scale = newDist / oldDist;
					matrix.set(savedMatrix);
					matrix.postScale(scale, scale, mid.x, mid.y);

				}
				invalidate();
			}
			break;
		}

		/*
		 * getting constant values of Matrix
		 */
		values = new float[9];
		matrix.getValues(values);

		/*
		 * getting reversed constant values of Matrix
		 */

		transx = (int) -(values[Matrix.MTRANS_X]);
		transy = (int) -(values[Matrix.MTRANS_Y]);

		/*
		 * getting Matrix's zoom level value
		 */

		ZoomLevel = values[Matrix.MSCALE_X];

		/*
		 * this if statements are changing layer mode depending on zoom level.
		 * so it will draw different sets of layers
		 */

		if (ZoomLevel > 0 && ZoomLevel < 0.30) {
			LayerMode = 1;
		}

		if (ZoomLevel >= 0.30 && ZoomLevel < 0.66) {
			LayerMode = 2;
		}

		if (ZoomLevel >= 0.66 && ZoomLevel < 2) {
			LayerMode = 3;

			Enableicons = false;
		}
		if (ZoomLevel >= 2) {
			LayerMode = 4;

			Enableicons = true; // enables showing icons
		}

		return gestureDetector.onTouchEvent(event);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

	}

	/*
	 * Methods "suburbs" reads information from file suburbs.txt , converts and
	 * returns ArrayList of nodes
	 */

	public ArrayList<Node> suburbs() throws IOException {
		ArrayList<Node> array = new ArrayList<Node>();
		InputStream is = this.getResources().openRawResource(R.raw.suburbs);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String currentLine = "";
		while (br.ready()) {
			currentLine = br.readLine();
			if (!currentLine.equals(null)) {
				Node n = new Node();
				String[] temp = currentLine.split(" ");
				n.setlat(Double.parseDouble(temp[0]));
				n.setlon(Double.parseDouble(temp[1]));
				currentLine = br.readLine();
				temp = currentLine.split(" ");
				n.setname(temp[0]);
				array.add(n);
			}
		}
		return array;
	}

	/*
	 * Method which is used for setting information about POI from the file,
	 * converts and returns an ArrayList of nodes
	 */

	public ArrayList<Node> setinfo(InputStream is) throws IOException {

		ArrayList<Node> array = new ArrayList<Node>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String currentLine = "";
		while (br.ready()) {
			currentLine = br.readLine();
			if (!currentLine.equals(null)) {
				Node n = new Node();

				String[] temp = currentLine.split(" ");
				n.setlat(Double.parseDouble(temp[1]));
				n.setlon(Double.parseDouble(temp[2]));

				ArrayList<Tag> tags = new ArrayList<Tag>();

				currentLine = br.readLine();
				temp = currentLine.split(" ");

				while (!temp[0].equals("/r")) {

					Tag tag = new Tag();
					tag.setk(temp[0]);

					StringBuilder sb = new StringBuilder();

					for (int i = 1; i < temp.length; i++) {
						sb.append(temp[i] + " ");
					}

					sb.replace(sb.length() - 1, sb.length(), "");

					tag.setv(sb.toString());
					tags.add(tag);
					currentLine = br.readLine();
					temp = currentLine.split(" ");
				}
				n.settags(tags);
				array.add(n);
			}
		}
		return array;
	}

	/*
	 * Method which checks if the user touched an icon and shows information
	 * about this certain POI
	 */

	private void checkpoi(ArrayList<Rect> rect, ArrayList<Node> poi, String name) {

		StringBuilder sb = new StringBuilder();
		sb.append("");

		for (int i = 0; i < rect.size(); i++) {
			if (rect.get(i).contains(
					(int) ((transx + (int) touchx) / ZoomLevel),
					(int) (((transy + (int) touchy) + 75) / ZoomLevel)) == true
					&& showinfo == true) {
				for (int j = 0; j < poi.get(i).gettags().size(); j++) {

					if (poi.get(i).gettags().get(j).getk().equals("name")) {
						sb.append(name + "\""
								+ poi.get(i).gettags().get(j).getv() + "\"\n");
					}
					if (poi.get(i).gettags().get(j).getk().equals("cuisine")) {
						sb.append("Type: " + poi.get(i).gettags().get(j).getv()
								+ "\n");
					}
					if (poi.get(i).gettags().get(j).getk().equals("phone")) {
						sb.append("Phone: "
								+ poi.get(i).gettags().get(j).getv() + "\n");
					}
					if (poi.get(i).gettags().get(j).getk()
							.equals("opening_hours")) {
						sb.append("Open hours: "
								+ poi.get(i).gettags().get(j).getv() + "\n");
					}
					if (poi.get(i).gettags().get(j).getk().equals("website")) {
						sb.append("Website: "
								+ poi.get(i).gettags().get(j).getv() + "\n");
					}
					if (poi.get(i).gettags().get(j).getk().equals("email")) {
						sb.append("Email: "
								+ poi.get(i).gettags().get(j).getv() + "\n");
					}
				}
				sb.append("\n");
			}
		}

		if (!sb.toString().equals("")) {

			if (sb.length() > 2) {
				sb.replace(sb.length() - 2, sb.length(), "");
			}
			Toast.makeText(t.getApplicationContext(), sb, Toast.LENGTH_LONG)
					.show();

			/*
			 * Thread with sleep to avoid big amount of Toasts at the same time
			 */

			showinfo = false;
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(3500);
						showinfo = true;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}

	}

	/*
	 * Method which sets rectangles according to POI location (boxes, places)
	 */

	private void iconlocation(ArrayList<Node> places, ArrayList<Rect> boxes) {
		for (int i = 0; i < places.size(); i++) {
			boxes.add(new Rect());
			boxes.get(i).set(
					(int) ((places.get(i).getlat() - e.minx) * e.x) - 4,
					(int) ((places.get(i).getlon() - e.miny) * e.y) - 4,
					(int) ((places.get(i).getlat() - e.minx) * e.x) + 4,
					(int) ((places.get(i).getlon() - e.miny) * e.y) + 4);
		}
	}

	/*
	 * Method for drawing icons (POI)
	 */
	private void drawicons(Canvas c, ArrayList<Rect> rect, Drawable pic) {

		for (int j = 0; j < rect.size(); j++) {
			pic.setBounds(rect.get(j).left, rect.get(j).top, rect.get(j).right,
					rect.get(j).bottom);
			pic.draw(c);
		}

	}

	/*
	 * Method which activates when user wants to go from point A to point B it
	 * sets a rectangle with a size of a screen it finds all the nodes around
	 * user's touched point with boundaries of rectangle, creates ArrayList of
	 * this nodes, calls another method to find the closest one from this
	 * ArrayList.
	 * 
	 * "settingpoints" value identifies which point (A or B) to declare. after
	 * setting both points it calls a method to find path between this two nodes
	 */
	void putpointAB() throws InterruptedException {

		settingpoints++;

		fornav.set(0, 0, this.getWidth(), this.getHeight());

		ArrayList<Node> close = new ArrayList<Node>();
		Point touch = new Point();

		touch.set((int) ((touchx + transx) / ZoomLevel), (int) ((touchy
				+ transy + 70) / ZoomLevel)); // setting coordinates for a point
												// which user touched

		/*
		 * checking through all navigation info if nodes present in rectangle
		 * around touched point and adding them to ArrayList
		 */
		for (int i = 0; i < e.nodes.size(); i++) {
			if (fornav.contains(
					(int) ((e.nodes.get(i).getlat() - e.minx) * e.x),
					(int) ((e.nodes.get(i).getlon() - e.miny) * e.y))) {

				close.add(e.nodes.get(i));
			}
		}

		Thread.sleep(300);

		if (close.isEmpty() == false) {
			if (settingpoints == 1) {

				point1 = findclosest(close, touch); // Point A

				t1x = (int) ((point1.getlat() - e.minx) * e.x); // Point A x
																// coordinates
				t1y = (int) ((point1.getlon() - e.miny) * e.y); // Point A y
																// coordinates

			}
			if (settingpoints == 2) {

				point2 = findclosest(close, touch); // Point B

				t2x = (int) ((point2.getlat() - e.minx) * e.x); // Point B x
																// coordinates
				t2y = (int) ((point2.getlon() - e.miny) * e.y); // Point B y
																// coordinates

				algo.Dijkstra(e.nodes, e.ways, e.edges, e.forvertexes, // calling
																		// path
																		// algorithm
						point1.getid(), point2.getid());

				path = algo.coolpath; // setting Arraylist of nodes which is
										// path
				settingpoints = 0;

				pathcreated = true;
				menuABclicked = false;
			}
		}

	}

	/*
	 * Basically this method is similiar to method "putpointAB()" but instead of
	 * Point A from previous one it takes corrent location coordintates
	 */

	public void putpointAC() throws InterruptedException {

		fornav.set(0, 0, this.getWidth(), this.getHeight());

		/*
		 * Here it sets information for touched point (point B)
		 */
		ArrayList<Node> close = new ArrayList<Node>();
		Point touch = new Point();

		touch.set((int) ((touchx + transx) / ZoomLevel), (int) ((touchy
				+ transy + 70) / ZoomLevel));

		for (int i = 0; i < e.nodes.size(); i++) {
			if (fornav.contains(
					(int) ((e.nodes.get(i).getlat() - e.minx) * e.x),
					(int) ((e.nodes.get(i).getlon() - e.miny) * e.y))) {

				close.add(e.nodes.get(i));
			}
		}
		Thread.sleep(300);

		if (close.isEmpty() == false) {

			point2 = findclosest(close, touch);

			t2x = (int) ((point2.getlat() - e.minx) * e.x);
			t2y = (int) ((point2.getlon() - e.miny) * e.y);

		}

		/*
		 * Here it sets information for current location coordinates and runs
		 * path algorithm
		 */

		ArrayList<Node> closecurr = new ArrayList<Node>();
		Point curr = new Point();

		curr.set((int) ((latitude - e.minx) * e.x),
				(int) ((longtitude - e.miny) * e.y));

		for (int i = 0; i < e.nodes.size(); i++) {
			if (fornav.contains(
					(int) ((e.nodes.get(i).getlat() - e.minx) * e.x),
					(int) ((e.nodes.get(i).getlon() - e.miny) * e.y))) {

				closecurr.add(e.nodes.get(i));
			}
		}
		Thread.sleep(300);

		if (close.isEmpty() == false) {

			point1 = findclosest(closecurr, curr);

			t1x = (int) ((point1.getlat() - e.minx) * e.x);
			t1y = (int) ((point1.getlon() - e.miny) * e.y);

			algo.Dijkstra(e.nodes, e.ways, e.edges, e.forvertexes,
					point1.getid(), point2.getid());

			path = algo.coolpath;

			pathcreated = true;

		}
		menuACclicked = false;
	}

	
	
	public void pathcoord(double x1 , double y1, double x2, double y2){
		
		Point pointA = new Point();
		ArrayList<Node> closeA = new ArrayList<Node>();
		
		pointA.set((int) ((x1 - e.minx)*e.x), (int) ((y1 - e.minx)*e.y));
		
		for (int i = 0; i < e.nodes.size(); i++) {
			if (fornav.contains(
					(int) ((e.nodes.get(i).getlat() - e.minx) * e.x),
					(int) ((e.nodes.get(i).getlon() - e.miny) * e.y))) {

				closeA.add(e.nodes.get(i));
			}
		}
		
		Point pointB = new Point();
		ArrayList<Node> closeB = new ArrayList<Node>();
		
		pointB.set((int) ((x1 - e.minx)*e.x), (int) ((y1 - e.minx)*e.y));
		
		for (int i = 0; i < e.nodes.size(); i++) {
			if (fornav.contains(
					(int) ((e.nodes.get(i).getlat() - e.minx) * e.x),
					(int) ((e.nodes.get(i).getlon() - e.miny) * e.y))) {

				closeB.add(e.nodes.get(i));
			}
		}
		
		if (closeA.isEmpty() == false && closeB.isEmpty() == false) {
			point1 = findclosest(closeA, pointA);

			t1x = (int) ((point1.getlat() - e.minx) * e.x);
			t1y = (int) ((point1.getlon() - e.miny) * e.y);

			point2 = findclosest(closeB, pointB);

			t2x = (int) ((point2.getlat() - e.minx) * e.x);
			t2y = (int) ((point2.getlon() - e.miny) * e.y);
			
			algo.Dijkstra(e.nodes, e.ways, e.edges, e.forvertexes,
					point1.getid(), point2.getid());

			path = algo.coolpath;

			pathcreated = true;
		}
		
		
	}
	
	/*
	 * A method which compares touched point with ArrayList of nodes and returns
	 * closest one
	 */

	private Node findclosest(ArrayList<Node> points, Point touch) {

		double finweight = 999;
		Node closest = new Node();

		for (int i = 0; i < points.size(); i++) {
			double x = ((points.get(i).getlat() - e.minx) * e.x);
			double y = ((points.get(i).getlon() - e.miny) * e.y);

			double finlat = (x - touch.x) * (x - touch.x);
			double finlon = (y - touch.y) * (y - touch.y);
			double weight = Math.sqrt(finlat + finlon);
			if (weight < finweight) {

				finweight = weight;
				closest = points.get(i);
			}
		}
		return closest;
	}

}