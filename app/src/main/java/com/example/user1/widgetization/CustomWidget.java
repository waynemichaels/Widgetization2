package com.example.user1.widgetization;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Math.ceil;

public class CustomWidget extends RelativeLayout {
    private LinearLayout contentLayout;

    private CardView card;

    //Types that the CustomWidget displays
    private final int smallText = 0;
    private final int bigText = 1;
    private final int smallImage = 2;
    private final int bigImage = 3;
    private final int percentage = 4;
    private final int currency = 5;
    private final int date = 6;
    private final int hLabelledValue = 7;
    private final int vLabelledValue = 8;
    private final int smallRatingsButton = 9;
    private final int bigRatingsButton = 10;
    private final int smallProgressBar = 11;
    private final int bigProgressBar = 12;
    private final int button = 13;
    private final int horizontalLine = 14;
    private final int verticalLine = 15;

    private final int noOfTypes = 16;

    //Row count and column count of the grid layout
    private int rowNumber = 0;
    private int columnNumber = 3;

    /*
    This variable will contain Span objects that are used to
    configure cells in a contentLayout e.g the length and span of the cell
    within each int[] array:
    [0] - row starting point
    [1] - column starting point
    [2] - row span
    [3] - column span
    */
    private ArrayList<int[]> cellSpans = new ArrayList<int[]>();
    private ArrayList<int[]> specs = new ArrayList<int[]>();
    private List widgetList = new ArrayList();

    //Contains the widget types based on their respective indexes above
    private ArrayList<Integer> gridContentWidgets = new ArrayList<Integer>();
    private List spansList = new ArrayList();

    /*
    Contains the configurations of horizontal borders
    with each array int[]
    [0] - row number of cell which the border will be start under
    [1] - column number of cell which the border will be start under
    [2] - the length of the border line
    */
    private ArrayList<int[]> horizontalBorders = new ArrayList<int[]>();
    /*
    Contains the configurations of vertical borders
    with each array int[]
    [0] - row number of cell which the border will be start to the right of
    [1] - column number of cell which the border will be start to the right of
    [2] - the length of the border line
    */
    private ArrayList<int[]> verticalBorders = new ArrayList<int[]>();
    private ArrayList<Integer> shrinkRows = new ArrayList<Integer>();

    private Map shrinkButtonStates = new HashMap();

    /*
    These 2 variables hold the calculated dimentions of the screen
    during initialization
    */
    private int screenWidth;
    private int screenHeight;

    ImageButton buttonInfo, buttonEdit, buttonDelete, buttonShrink;
    CircleImageView imageProfile;
    TextView textviewTitle;
    TextView textviewDescription;

    boolean mainButtonShrink = false;

    //Initialization of testing values
    {
        /**cellSpans.add(new int[]{0,0,1,3});
        cellSpans.add(new int[]{3,0,1,3});
        cellSpans.add(new int[]{5,0,1,3});
        cellSpans.add(new int[]{5,0,1,3});
        cellSpans.add(new int[]{6,0,1,3});
        cellSpans.add(new int[]{7,0,1,3});
        cellSpans.add(new int[]{13,0,1,2});**/
        
        //horizontalBorders.add(new int[]{0,0,2});
        //horizontalBorders.add(new int[]{2,1,3});

        //verticalBorders.add(new int[]{0,0,3});
        //verticalBorders.add(new int[]{1,1,3});

        //shrinkRows.add(1);
        //shrinkRows.add(3);
        
        /**gridContentWidgets.add(1);
        gridContentWidgets.add(0);
        gridContentWidgets.add(0);
        gridContentWidgets.add(0);
        gridContentWidgets.add(11);
        gridContentWidgets.add(11);
        gridContentWidgets.add(11);
        gridContentWidgets.add(1);
        gridContentWidgets.add(8);
        gridContentWidgets.add(8);
        gridContentWidgets.add(8);
        gridContentWidgets.add(1);
        gridContentWidgets.add(0);
        gridContentWidgets.add(13);
        gridContentWidgets.add(2);
        gridContentWidgets.add(2);
        gridContentWidgets.add(2);
        gridContentWidgets.add(3);
        gridContentWidgets.add(3);
        gridContentWidgets.add(3);
        gridContentWidgets.add(4);
        gridContentWidgets.add(4);
        gridContentWidgets.add(4);
        gridContentWidgets.add(5);
        gridContentWidgets.add(5);
        gridContentWidgets.add(5);
        gridContentWidgets.add(6);
        gridContentWidgets.add(6);
        gridContentWidgets.add(6);
        gridContentWidgets.add(7);
        gridContentWidgets.add(7);
        gridContentWidgets.add(8);
        gridContentWidgets.add(8);
        gridContentWidgets.add(8);
        gridContentWidgets.add(9);
        gridContentWidgets.add(9);
        gridContentWidgets.add(9);**/
    }

    public CustomWidget(Context context) {
        super(context);
        init(null);
    }

    public CustomWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /** This method takes in a row and column values as the parameters i and j and checks whether
     * the cell is spanned horizontally in the arrayList spannedCell that contains a list of all
     * the spans greater than 1
     * @param i row value
     * @param j column value
     * @return returns the span length of the current cell
     */
    private int spannedHorizontally(int i, int j){
        for(int[] spannedCell : cellSpans) {
            if (spannedCell[0] == i && spannedCell[1] == j && spannedCell[3] > 1) {
                return spannedCell[3];
            }
        }
        return 1;
    }

    /** This method takes in a row and column values as the parameters i and j and checks whether
     * the cell is spanned vertically in the arrayList spannedCell that contains a list of all
     * the spans greater than 1
     * @param i row value
     * @param j column value
     * @return returns the span length of the current cell
     */
    private int spannedVertically(int i, int j){
        for(int[] spannedCell : cellSpans) {
            if (spannedCell[0] == i && spannedCell[1] == j && spannedCell[2] > 1) {
                return spannedCell[2];
            }
        }
        return 1;
    }

    /** This method takes in a row and column values as the parameters i and j and checks whether
     * the cell has horizontal borders from the arrayList horizontalBorders that contains a list of all
     * the cells with horizontal borders and how far they extend
     * @param i row value
     * @param j column value
     * @return returns true if the cell has a horizontal border
     */
    private boolean hasHorizontalBorder(int i, int j){
        for(int[] brd : horizontalBorders) {
            if (brd[0] == i) {
                if(j < (brd[1] + brd[2]) && j >= brd[1]){
                    return true;
                }
            }
        }
        return false;
    }

    /** This method takes in a row and column values as the parameters i and j and checks whether
     * the cell has vertical borders from the arrayList verticalBorders that contains a list of all
     * the cells with vertical borders and how far they extend
     * @param i row value
     * @param j column value
     * @return returns true if the cell has a horizontal border
     */

    private boolean hasVerticalBorder(int i, int j){
        for(int[] brd : verticalBorders) {
            if (brd[1] == j) {
                if(i < (brd[0] + brd[2]) && i >= brd[0]){
                    return true;
                }
            }
        }
        return false;
    }

    /** This method takes in a row as the parameters i and j and checks whether
     * the row has a shrink button from the arrayList shrinkRows that contains a list of all
     * the rows with shrink buttons
     * @param i row value
     * @return returns true if the cell has a horizontal border
     */
    private boolean hasShrink(int i){
        if(shrinkRows.contains(i)){
            return true;
        }
        return false;
    }

    /**
     * This method simply makes a horizontal line then returns it
     * @return returns a View object that contains the horizontal line
     */
    private View makeHorizontalLine(){
        LayoutInflater  inflater = LayoutInflater.from(contentLayout.getContext());
        View lineView = (View)inflater.inflate(R.layout.horizontal_line, null);
        return  lineView;
    }

    /**
     * This method simply makes a vertical line then returns it
     * @return returns a View object that contains the vertical line
     */
    private View makeVerticalLine(){
        LayoutInflater  inflater = LayoutInflater.from(contentLayout.getContext());
        View lineView = (View)inflater.inflate(R.layout.vertical_line, null);
        return  lineView;
    }

    /**
     * This method creates an alert popup of Neutral kind on pressing the icons button
     * @param message this is the message that will be displayed on the popup dialog
     */
    private void callAlertDialog(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(card.getContext());
        alertDialogBuilder.setTitle( "Ebelle" );
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNeutralButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void callCloseMessageDialog(String message, final String methodName){
        AlertDialog.Builder dialog = new AlertDialog.Builder(card.getContext());
        dialog.setTitle( "Ebelle" ).setMessage(message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                         dialoginterface.cancel();
                          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        close();
                    }
                }).show();
    }

    private void shrinkCard(boolean state){
        if(state) {
            contentLayout.setVisibility(GONE);
        }else{
            contentLayout.setVisibility(VISIBLE);
        }
    }

    private void switchShrinkButton(boolean state){
        if(state) {
            buttonShrink.setImageResource(R.drawable.collapse_arrow);
        }else{
            buttonShrink.setImageResource(R.drawable.expand_arrow);
        }
    }

    public void addViews(int... viewTypes){
        gridContentWidgets.clear();
        for(int vw:viewTypes){
            gridContentWidgets.add(vw);
        }
        buildWidgets();
        makeLayout();
        setEventListeners();
    }

    public void addColumnSpans(int[]... spns){
        cellSpans.clear();
        for(int[] sp:spns){
            cellSpans.add(sp);
        }
    }

    public void setSmallText(int i, int j, String text){
        TextView txt = (TextView) getTextview(i,j);
        txt.setText(text);
    }

    public void setBigText(int i, int j, String text){
        TextView txt = (TextView) getTextview(i,j);
        txt.setText(text);
    }

    public void setSmallImage(int i, int j, int drawableId){
        ImageView img = (ImageView) getImageview(i,j);
        Drawable myIcon = getResources().getDrawable(drawableId);
        img.setImageDrawable(myIcon);
    }

    public void setBigImage(int i, int j, int drawableId){
        ImageView img = (ImageView) getImageview(i,j);
        Drawable myIcon = getResources().getDrawable(drawableId);
        img.setImageDrawable(myIcon);
    }

    public void setPercentage(int i, int j, int percent, String label){
        TextView txtTop = (TextView) getTextViewTop(i,j);
        TextView txtBottom = (TextView) getTextViewBottom(i,j);
        txtTop.setText(""+percent+"%");
        txtBottom.setText(label);
    }

    public void setCurrency(int i, int j, int amount, String label){
        TextView txtTop = (TextView) getTextViewRight(i,j);
        TextView txtBottom = (TextView) getTextViewLeft(i,j);
        txtTop.setText("ksh "+amount);
        txtBottom.setText(label);
    }

    public void setDate(int i, int j, Date dt){
        if (dt==null){
            dt = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateTxt = sdf.format(dt);
        TextView txt = (TextView) getTextview(i,j);
        txt.setText(dateTxt);
    }

    public void setHLValue(int i, int j, String leftString, String rightString){
        TextView txtLeft = (TextView) getTextViewLeft(i,j);
        TextView txtRight = (TextView) getTextViewRight(i,j);
        txtLeft.setText(leftString);
        txtRight.setText(rightString);
    }

    public void setVLValue(int i, int j, String topString, String bottomString){
        TextView txtTop = (TextView) getTextViewTop(i,j);
        TextView txtBottom = (TextView) getTextViewBottom(i,j);
        txtTop.setText(topString);
        txtBottom.setText(bottomString);
    }

    public void setProgress(int i, int j,int progress, String label){
        TextView innerText = (TextView) getProgressBarInnerText(i,j);
        TextView labelText = (TextView) getProgressBarLabel(i,j);
        ProgressBar prog = (ProgressBar) getProgressBar(i,j);
        innerText.setText(""+progress);
        labelText.setText(label);
        prog.setProgress(progress);
    }

    public void setRating(int i, int j,int value){
        RatingBar prog = (RatingBar) getRatingBar(i,j);
        prog.setRating((float)value);
    }

    public void setButtonListener(int i, int j, OnClickListener btnListener){
        Button btn = (Button) getButton(i,j);
        btn.setOnClickListener(btnListener);
    }

    public void setButtonText(int i, int j, String btnText){
        Button btn = (Button) getButton(i,j);
        btn.setText(btnText);
    }

    public void removeProfilePic(){
        imageProfile.setVisibility(View.GONE);
    }

    public void removeTitleDescription(){
        textviewDescription.setVisibility(View.GONE);
    }

    public void removeTitle(){
        textviewTitle.setVisibility(View.GONE);
        invalidate();
    }

    public void setProfilePic(int resource){
        imageProfile.setVisibility(View.VISIBLE);
        imageProfile.setImageResource(resource);
    }

    public void setTitle(String title){
        textviewTitle.setVisibility(View.VISIBLE);
        textviewTitle.setText(title);
    }

    public void setDescription(String description){
        textviewDescription.setVisibility(View.VISIBLE);
        textviewDescription.setText(description);
    }

    public void close(){
        ((ViewManager)card.getParent()).removeView(card);
    }

    private void addShrinkButtonEvent(final ImageButton btn, final int index){
        btn.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                if (btn.getId() == R.id.button_Down){
                    shrinkButtonStates.put(index,true);
                }else
                    shrinkButtonStates.put(index,false);
                makeLayout();
            }
        });
    }

    public TextView getTextview(int i, int j){
        int index = getIndex(i,j);
        return (TextView) widgetList.get(index);
    }

    public ImageView getImageview(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (ImageView)currView.findViewById(R.id.circularImage);
    }

    public TextView getTextViewTop(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (TextView)currView.findViewById(R.id.textViewTop);
    }

    public TextView getTextViewBottom(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (TextView)currView.findViewById(R.id.textViewBottom);
    }

    public TextView getTextViewLeft(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (TextView)currView.findViewById(R.id.textViewLeft);
    }

    public TextView getTextViewRight(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (TextView)currView.findViewById(R.id.textViewRight);
    }

    public ProgressBar getProgressBar(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (ProgressBar) currView.findViewById(R.id.circular_progressbar);
    }

    public TextView getProgressBarInnerText(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (TextView) currView.findViewById(R.id.innerText);
    }

    public TextView getProgressBarLabel(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (TextView) currView.findViewById(R.id.outerText);
    }

    public RatingBar getRatingBar(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (RatingBar) currView.findViewById(R.id.ratingsCustom);
    }

    public Button getButton(int i, int j){
        int index = getIndex(i,j);
        View currView = (View) widgetList.get(index);
        return (Button) currView.findViewById(R.id.customButton);
    }


    /**public ImageView getImageView(int i, int j){
        int index = getIndex(i,j);
    }**/

    private int getIndex(int i, int j){
        int index = 0;
        for(int[] cell:specs){
            /**Log.d("HERE!!!!!","INDEX :"+index+" ROW :"+cell[0]+" COLUMN :"+
                    cell[1]+" ROWSPAN :"+cell[2]+" COLUMNSPAN :"+
                    cell[3]);**/
            if(cell[0] == i && cell[1] == j){
                return index;
            }
            index++;
        }
        return index;
    }

    private void setEventListeners(){
        buttonInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                callAlertDialog("HERE!!!!!!!!!!!!!!!!!");
            }
        });

        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                callCloseMessageDialog("Are you sure you want to delete","close");
            }
        });

        buttonShrink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainButtonShrink = !mainButtonShrink;
                shrinkCard(mainButtonShrink);
                switchShrinkButton(mainButtonShrink);
            }
        });
    }

    /**
     * This method loops through the arrayList gridContentWidgets, creates the necessary object then add
     * the objects to the arrayList widgetList
     */
    private void buildWidgets(){
        buttonDelete = (ImageButton)card.findViewById(R.id.button_Delete);
        buttonEdit = (ImageButton)card.findViewById(R.id.button_Edit);
        buttonInfo = (ImageButton)card.findViewById(R.id.button_Info);
        buttonShrink = (ImageButton)card.findViewById(R.id.button_Shrink);
        imageProfile = (CircleImageView) card.findViewById(R.id.image_ProfileImage);
        textviewTitle = (TextView)card.findViewById(R.id.textView_Title);
        textviewDescription = (TextView) card.findViewById(R.id.textView_Description);
        int index = 0;
        for (int i:gridContentWidgets){
            switch (i){
                case smallText:
                    LayoutInflater  txtInflater = LayoutInflater.from(contentLayout.getContext());
                    TextView txt = (TextView)txtInflater.inflate(R.layout.textview_small, null);

                    txt.setGravity(Gravity.CENTER);
                    txt.setText("Appointments");
                    txt.setId(index);
                    widgetList.add(txt);
                    break;
                case bigText:
                    LayoutInflater  txtInflater2 = LayoutInflater.from(contentLayout.getContext());
                    TextView txt2 = (TextView)txtInflater2.inflate(R.layout.textview_small, null);

                    txt2.setGravity(Gravity.CENTER);
                    txt2.setTypeface(null, Typeface.BOLD);
                    txt2.setText("100");
                    widgetList.add(txt2);
                    break;
                case smallImage:
                    LayoutInflater  inflater = LayoutInflater.from(contentLayout.getContext());
                    View img = inflater.inflate(R.layout.circle_image_small, null);

                    widgetList.add(img);
                    break;
                case bigImage:
                    LayoutInflater  inflater2 = LayoutInflater.from(contentLayout.getContext());
                    View img2 = inflater2.inflate(R.layout.circle_image_big, null);

                    widgetList.add(img2);
                    break;
                case percentage:
                    LayoutInflater  inflater3 = LayoutInflater.from(contentLayout.getContext());
                    View labelledText = inflater3.inflate(R.layout.vertical_labelled_text, null);

                    TextView txtTop = labelledText.findViewById(R.id.textViewTop);
                    TextView txtBottom = labelledText.findViewById(R.id.textViewBottom);

                    txtTop.setText("100%");
                    txtBottom.setText("Pedicure");

                    widgetList.add(labelledText);
                    break;
                case currency:
                    LayoutInflater  inflater4 = LayoutInflater.from(contentLayout.getContext());
                    View labelledText2 = inflater4.inflate(R.layout.horizontal_labelled_text, null);

                    TextView txtLeftTxt = labelledText2.findViewById(R.id.textViewLeft);
                    TextView txtRightTxt = labelledText2.findViewById(R.id.textViewRight);

                    txtLeftTxt.setText("NAILS");
                    txtRightTxt.setText("ksh 100000");

                    widgetList.add(labelledText2);
                    break;
                case date:
                    LayoutInflater  txtInflater3 = LayoutInflater.from(contentLayout.getContext());
                    TextView txtDate = (TextView)txtInflater3 .inflate(R.layout.textview_small, null);

                    txtDate.setGravity(Gravity.CENTER);

                    Date dNow = new Date( );
                    SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy");

                    txtDate.setText(ft.format(dNow));

                    widgetList.add(txtDate);
                    break;
                case hLabelledValue:
                    LayoutInflater  inflater5 = LayoutInflater.from(contentLayout.getContext());
                    View labelledText3 = inflater5.inflate(R.layout.horizontal_labelled_text, null);

                    TextView txtLeft = labelledText3.findViewById(R.id.textViewLeft);
                    TextView txtRight = labelledText3.findViewById(R.id.textViewRight);

                    txtLeft.setText("STYLE");
                    txtRight.setText("Pedicure");

                    widgetList.add(labelledText3);
                    break;
                case vLabelledValue:
                    LayoutInflater  inflater6 = LayoutInflater.from(contentLayout.getContext());
                    View labelledText4 = inflater6.inflate(R.layout.vertical_labelled_text, null);

                    TextView txtTop4 = labelledText4.findViewById(R.id.textViewTop);
                    TextView txtBottom4 = labelledText4.findViewById(R.id.textViewBottom);

                    txtTop4.setText("NAILS");
                    txtBottom4.setText("style");

                    widgetList.add(labelledText4);
                    break;
                case smallRatingsButton:
                    LayoutInflater  ratingsInflater = LayoutInflater.from(contentLayout.getContext());
                    View ratingView = (View) ratingsInflater.inflate(R.layout.small_ratingsbar, null);
                    widgetList.add(ratingView);
                    break;
                case bigRatingsButton:
                    LayoutInflater  ratingsInflater2 = LayoutInflater.from(contentLayout.getContext());
                    View ratingView2 = (View) ratingsInflater2.inflate(R.layout.big_ratingsbar, null);
                    widgetList.add(ratingView2);
                    break;
                case smallProgressBar:
                    LayoutInflater  inflater7 = LayoutInflater.from(contentLayout.getContext());
                    View progressView = (View)inflater7.inflate(R.layout.small_progressbar, null);
                    widgetList.add(progressView);
                    break;
                case bigProgressBar:
                    LayoutInflater  inflater8 = LayoutInflater.from(contentLayout.getContext());
                    View progressView2 = (View)inflater8.inflate(R.layout.big_progressbar, null);
                    widgetList.add(progressView2);
                    break;
                case button:
                    LayoutInflater  inflater9 = LayoutInflater.from(contentLayout.getContext());
                    View buttonView = (View)inflater9.inflate(R.layout.button_layout, null);
                    widgetList.add(buttonView);
                    break;
                case horizontalLine:
                    LayoutInflater  inflater10 = LayoutInflater.from(contentLayout.getContext());
                    View lineView = (View)inflater10.inflate(R.layout.horizontal_line, null);
                    widgetList.add(lineView);
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void makeLayout(){
        /*
        This will hold the spec objects for individual cells
        It will be populated in the loop below
        */
        int index = 0;
        int i = 0;

        //Loop through all the items to be added into the layout


        //This is a label that will be used to break out of the outer loop since we have 2 nested for loops
        //The use of 'outerloop' can be seen below
        outerloop:
        do{
            for(int j=0 ; j<columnNumber; j++){
                /*
                This is so that the row number can be generated dynamically
                The do while loop is stopped when all the widgets have been
                configured
                */
                if(!(index < gridContentWidgets.size())){
                    break outerloop;
                }
                /*
                int [] cellSpec objects are used to configure individual cells
                [0] - row starting point
                [1] - column starting point
                [2] - row span
                [3] - column span
                */
                int [] cellSpec = new int[4];
                boolean cellAdded = false;
                /*
                Loop through an array of cell span configurations to check
                if the current cells has been set to have a cell span greater
                than 1
                NB: We initialized at the top
                */
                if(spannedHorizontally(i,j) > 1 || spannedVertically(i,j) > 1){

                    //set the row and column spec if any
                    cellSpec[0] = i;
                    cellSpec[1] = j;
                    cellSpec[2] = spannedVertically(i,j);
                    cellSpec[3] = spannedHorizontally(i,j);
                    cellAdded = true;
                    j = j + cellSpec[3]-1;
                    /**Log.d("HERE!!!!!","INDEX :"+index+" ROW :"+i+" COLUMN :"+
                            j+" ROWSPAN :"+cellSpec[2]+" COLUMNSPAN :"+
                            cellSpec[3]);**/
                }
                /*
                check if the cell has already been speced above, if not
                spec the cell leaving the row and column spans as default
                */
                if (!cellAdded){
                    cellSpec[0] = i;
                    cellSpec[1] = j;
                    cellSpec[2] = 1;
                    cellSpec[3] = 1;
                }

                //add the     android:layout_gravity="center_horizontal"cell configuration to the global variable specs
                specs.add(cellSpec);
                ++index;
                /**Log.d("HERE!!!!!","INDEX :"+index+" ROW :"+i+" COLUMN :"+
                 j+" ROWSPAN :"+cellSpec[2]+" COLUMNSPAN :"+
                 cellSpec[3]);**/
            }
            ++i;
        }while(true);

        //Remove all the views from the grid layout we will populate
        contentLayout.removeAllViews();

        /*
        contentLayout.LayoutParams object contains the configurations of
        the entire layout as a whole
        */
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
        param.height = LayoutParams.MATCH_PARENT;
        param.width = LayoutParams.MATCH_PARENT;
        contentLayout.setLayoutParams(param);

        //update to the right number of rows
        rowNumber = i+1;

        //This will be used to check how many items have been added
        int index2 = 0;

        //This is a label that will be used to break out of the outer loop since we have 2 nested for loops
        //The use of 'outerloop2' can be seen below
        outerloop2:
        for(int k = 0; k < rowNumber; k++){
            //rowlayout will be used to fill items of each row
            LinearLayout rowLayout = new LinearLayout(contentLayout.getContext());

            if(hasShrink(k)){
                LayoutInflater  shrinkButtonInflater = LayoutInflater.from(contentLayout.getContext());
                if(shrinkButtonStates.containsKey(k)){
                    if ((boolean)shrinkButtonStates.get(k)){
                        View shrinkView = (View) shrinkButtonInflater.inflate(R.layout.button_arrowup, null);
                        ImageButton btn = (ImageButton) shrinkView.findViewById(R.id.button_Up);
                        addShrinkButtonEvent(btn, k);
                        contentLayout.addView(shrinkView);
                        for(int n = 0; n < columnNumber; n++){
                            int currentHorizontalWeight = specs.get(index2)[3];
                            if(currentHorizontalWeight > 1){
                                n = n+(currentHorizontalWeight-1);
                            }
                            ++index2;
                        }
                        continue;
                    }
                }

                View shrinkView = (View) shrinkButtonInflater.inflate(R.layout.button_arrowdown, null);
                ImageButton btn = (ImageButton) shrinkView.findViewById(R.id.button_Down);
                addShrinkButtonEvent(btn, k);
                shrinkButtonStates.put(k,false);
                contentLayout.addView(shrinkView);
            }

            LinearLayout.LayoutParams currentRowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            currentRowParams.gravity = Gravity.FILL;
            rowLayout.setLayoutParams(currentRowParams);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);

            //loop through each cell in the row
            for(int n = 0; n < columnNumber; n++){
                //break out of the outer for loop if all the items have been added
                if(!(index2 < gridContentWidgets.size())){
                    break outerloop2;
                }

                //Add an initial space object to seperate the view from the left margin
                Space spacer = new Space(rowLayout.getContext());
                LinearLayout.LayoutParams currentSpaceParams  =
                        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                currentSpaceParams.weight = 1;
                spacer.setLayoutParams(currentSpaceParams);
                rowLayout.addView(spacer, currentSpaceParams);

                //Create parameters to be used for the items to be added in each cell
                LinearLayout.LayoutParams currentCellParams  = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                int horizontalWeight = specs.get(index2)[3];
                currentCellParams.weight = horizontalWeight;
                currentCellParams.gravity = Gravity.CENTER_VERTICAL;
                int currWidth = screenWidth>0 ? screenWidth/3 : 0;
                currentCellParams.width = currWidth*horizontalWeight;

                //create layout and parameters to be used for adding horizontal lines to each cell
                LinearLayout lineLayout = new LinearLayout(contentLayout.getContext());
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lineParams.gravity = Gravity.CENTER_VERTICAL;
                lineLayout.setLayoutParams(lineParams);
                lineLayout.setOrientation(LinearLayout.VERTICAL);

                /*
                Check if the object of the current cell is of the types
                smallText = 0;
                bigText = 1;
                date = 6;
                This same check will also be done for the other types below

                THE SAME ACTIONS ARE REPEATED FOR ALL TYPES
                */
                if(gridContentWidgets.get(index2) == 0 ||
                        gridContentWidgets.get(index2) == 1 ||
                        gridContentWidgets.get(index2) == 6){
                    //currentCellParams.width = screenWidth/columnNumber;
                    //get the view from the array list widgetList
                    TextView currentTxt = (TextView)widgetList.get(index2);
                    currentTxt.setLayoutParams(currentCellParams);
                    //remove the views parent !!!This is because of a layout error
                    if(currentTxt.getParent()!=null)
                        ((ViewGroup)currentTxt.getParent()).removeView(currentTxt);
                    //Add the view to a layout that will hold the view and horizontal border beneath it
                    lineLayout.addView(currentTxt, currentCellParams);
                    //Check if this current cell has a horizontal border beneath it then add if necessary
                    if(hasHorizontalBorder(k, n)){
                        View lineView = makeHorizontalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 5;
                        lineView.setLayoutParams(currentLineParams);

                        lineLayout.addView(lineView, currentLineParams);
                    }
                    rowLayout.addView(lineLayout, lineParams);
                    //Check if this current cell has a vertical border to the right of it then add if necessary
                    if(hasVerticalBorder(k, n)){
                        LinearLayout.LayoutParams currentLineSpaceParams  =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        currentLineSpaceParams.weight = 1;
                        Space spacerLine = new Space(rowLayout.getContext());
                        rowLayout.addView(spacerLine, currentLineSpaceParams);

                        View lineView = makeVerticalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 10;
                        lineView.setLayoutParams(currentLineParams);

                        rowLayout.addView(lineView, currentLineParams);
                    }
                }
                else if(gridContentWidgets.get(index2) == 4||
                        gridContentWidgets.get(index2) == 5||
                        gridContentWidgets.get(index2) == 7||
                        gridContentWidgets.get(index2) == 8){
                    View currentView = (View)widgetList.get(index2);
                    currentView.setLayoutParams(currentCellParams);
                    if(currentView.getParent()!=null)
                        ((ViewGroup)currentView.getParent()).removeView(currentView);
                    lineLayout.addView(currentView, currentCellParams);
                    if(hasHorizontalBorder(k, n)){
                        View lineView = makeHorizontalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 5;
                        lineView.setLayoutParams(currentLineParams);

                        lineLayout.addView(lineView, currentLineParams);
                    }
                    rowLayout.addView(lineLayout, lineParams);
                    if(hasVerticalBorder(k, n)){
                        LinearLayout.LayoutParams currentLineSpaceParams  =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        currentLineSpaceParams.weight = 1;
                        Space spacerLine = new Space(rowLayout.getContext());
                        rowLayout.addView(spacerLine, currentLineSpaceParams);

                        View lineView = makeVerticalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 10;
                        lineView.setLayoutParams(currentLineParams);

                        rowLayout.addView(lineView, currentLineParams);
                    }
                }
                else if(gridContentWidgets.get(index2) == 2||
                        gridContentWidgets.get(index2) == 3){
                    View currentImg = (View)widgetList.get(index2);
                    currentImg.setLayoutParams(currentCellParams);
                    if(currentImg.getParent()!=null)
                        ((ViewGroup)currentImg.getParent()).removeView(currentImg);
                    lineLayout.addView(currentImg, currentCellParams);
                    if(hasHorizontalBorder(k, n)){
                        View lineView = makeHorizontalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 5;
                        lineView.setLayoutParams(currentLineParams);

                        lineLayout.addView(lineView, currentLineParams);
                    }
                    rowLayout.addView(lineLayout, lineParams);
                    if(hasVerticalBorder(k, n)){
                        LinearLayout.LayoutParams currentLineSpaceParams  =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        currentLineSpaceParams.weight = 1;
                        Space spacerLine = new Space(rowLayout.getContext());
                        rowLayout.addView(spacerLine, currentLineSpaceParams);

                        View lineView = makeVerticalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 10;
                        lineView.setLayoutParams(currentLineParams);

                        rowLayout.addView(lineView, currentLineParams);
                    }
                }
                else if(gridContentWidgets.get(index2) == 9||
                        gridContentWidgets.get(index2) == 10){
                    View currentRating = (View) widgetList.get(index2);
                    currentRating.setLayoutParams(currentCellParams);

                    if(currentRating.getParent()!=null)
                        ((ViewGroup)currentRating.getParent()).removeView(currentRating);

                    lineLayout.addView(currentRating, currentCellParams);
                    if(hasHorizontalBorder(k, n)){
                        View lineView = makeHorizontalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 5;
                        lineView.setLayoutParams(currentLineParams);

                        lineLayout.addView(lineView, currentLineParams);
                    }
                    rowLayout.addView(lineLayout, lineParams);
                    if(hasVerticalBorder(k, n)){
                        LinearLayout.LayoutParams currentLineSpaceParams  =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        currentLineSpaceParams.weight = 1;
                        Space spacerLine = new Space(rowLayout.getContext());
                        rowLayout.addView(spacerLine, currentLineSpaceParams);

                        View lineView = makeVerticalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 10;
                        lineView.setLayoutParams(currentLineParams);

                        rowLayout.addView(lineView, currentLineParams);
                    }
                }
                else if(gridContentWidgets.get(index2) == 11||
                        gridContentWidgets.get(index2) == 12||
                        gridContentWidgets.get(index2) == 13){
                    View currentView = (View)widgetList.get(index2);
                    currentView.setLayoutParams(currentCellParams);

                    if(currentView.getParent()!=null)
                        ((ViewGroup)currentView.getParent()).removeView(currentView);
                    lineLayout.addView(currentView, currentCellParams);
                    if(hasHorizontalBorder(k, n)){
                        View lineView = makeHorizontalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 5;
                        lineView.setLayoutParams(currentLineParams);

                        lineLayout.addView(lineView, currentLineParams);
                    }
                    rowLayout.addView(lineLayout, lineParams);
                    if(hasVerticalBorder(k, n)){
                        LinearLayout.LayoutParams currentLineSpaceParams  =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        currentLineSpaceParams.weight = 1;
                        Space spacerLine = new Space(rowLayout.getContext());
                        rowLayout.addView(spacerLine, currentLineSpaceParams);

                        View lineView = makeVerticalLine();
                        LinearLayout.LayoutParams currentLineParams =
                                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                        currentLineParams.weight = 1;
                        currentLineParams.topMargin = 10;
                        currentLineParams.bottomMargin = 10;
                        lineView.setLayoutParams(currentLineParams);

                        rowLayout.addView(lineView, currentLineParams);
                    }
                }

                horizontalWeight = specs.get(index2)[3];
                if(horizontalWeight > 1){
                    n = n+(horizontalWeight-1);
                }
                ++index2;
            }
            //Add a spacer object at the end of the row
            LinearLayout.LayoutParams currentSpaceParams  =
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            currentSpaceParams.weight = 1;
            Space spacerLast = new Space(rowLayout.getContext());
            rowLayout.addView(spacerLast, currentSpaceParams);

            contentLayout.addView(rowLayout);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void init(@Nullable AttributeSet set){
        inflate(getContext(),R.layout.card_layout,this);
        contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        card = findViewById(R.id.cardParent);

        buildWidgets();
        makeLayout();
        setEventListeners();

        //through this ViewTreeObserver object's addOnPreDrawListener event listener we can get the dimensions of the screen
        //as soon as the layout dimensions are calculated
        ViewTreeObserver vto = contentLayout.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public boolean onPreDraw() {
                card.getViewTreeObserver().removeOnPreDrawListener(this);
                screenHeight = card.getMeasuredHeight();
                screenWidth = card.getMeasuredWidth();
                buildWidgets();
                makeLayout();
                setEventListeners();
                return true;
            }

        });

        int b = 0;

        /**for(int[] k: specs){
            Log.d("HERE!!!!!","INDEX :"+b+" ROW :"+k[0]+" COLUMN :"+
                    k[1]+" ROWSPAN :"+k[2]+" COLUMNSPAN :"+
                    k[3]);
            b++;
        }**/
    }
}
