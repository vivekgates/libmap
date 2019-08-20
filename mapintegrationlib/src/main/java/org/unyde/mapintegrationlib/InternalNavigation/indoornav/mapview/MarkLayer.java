package org.unyde.mapintegrationlib.InternalNavigation.indoornav.mapview;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.Path_node;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.graph.HipsterGraph;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.problem.SearchProblem;
import org.unyde.mapintegrationlib.R;

import java.util.List;


public class MarkLayer extends MapBaseLayer {

    private List<PointF> marks;
    private List<String> marksName;
    private List<String> nodeName;
    private MarkIsClickListener listener;
    private Path mPath;

    public int first_point_x;
    public int first_point_y;

    public float rad1,rad2;

    public int second_point_x;
    public int second_point_y;

    public  float navigator_rotate;

    public String path_node_array[];
    public boolean via_locate = false;

    private Bitmap bmpMark, bmpMarkTouch,navigator_mark,routeStartBmp,routeEndBmp,iamheremarker;

    public float navigator_position_x,navigator_position_y,navigator_position_last_x,navigator_position_last_y,navigator_position_target_x,navigator_position_target_y;
    public Path_node path_node;
    private float radiusMark;
    private boolean isClickMark = false;
    private int num = -1;
    public Bitmap resizedBitmap;
    private Paint paint;
    HipsterGraph<String, Double> graph;
    SearchProblem p;
    Boolean isForImHere;

    //urhere
    public float urhere_x = 0,urhere_y = 0;
    public Context my_context = null;
    public MarkLayer(MapView mapView) {
        this(mapView, null, null,null,null,null);
    }

    public MarkLayer(MapView mapView, List<PointF> marks, List<String> marksName, List<String> nodeName, Context c, Boolean isForImHere) {
        super(mapView);
        this.marks = marks;
        this.marksName = marksName;
        this.nodeName = nodeName;
        this.isForImHere=isForImHere;
        this.my_context=c;
        setContext(c);
        initLayer();




    }

    public  void setContext(Context c)
    {
        my_context = c;
    }



    private void initLayer() {
        radiusMark = setValue(10f);


        bmpMark = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.pinshadowindoormap);
        bmpMarkTouch = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.pinshadowindoormap);
        routeStartBmp = BitmapFactory.decodeResource(mapView.getResources(),
                R.drawable.start_point);
        routeEndBmp = BitmapFactory.decodeResource(mapView.getResources(),
                R.drawable.end_point);
        navigator_mark = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.circle2);
       /* if(isForImHere)
        {
            iamheremarker = BitmapFactory.decodeResource(mapView.getResources(),R.mipmap.iamhere);
        }*/

       /* Matrix matrix = new Matrix();

        // rotate the Bitmap
        matrix.postRotate(navigator_rotate);

        // recreate the new Bitmap
        resizedBitmap = Bitmap.createBitmap(navigator_mark, 0, 0,
                navigator_mark.getWidth(), navigator_mark.getHeight(), matrix, true);*/
        paint = new Paint();
        mPath = new Path();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    public void onTouch(MotionEvent event) {
        Float tempdict = 1000f;
        int tempselected_index = -1;
        if (event.getPointerCount() == 1) {

        if (marks != null) {
            if (!marks.isEmpty()) {
                float[] goal = mapView.convertMapXYToScreenXY(event.getX(), event.getY());
                for (int i = 0; i < marks.size(); i++) {
//                    if (MapMath.getDistanceBetweenTwoPoints(goal[0], goal[1],
//                            marks.get(i).x - bmpMark.getWidth() / 2, marks.get(i).y - bmpMark
//                                    .getHeight() / 2) <= 30) {
                    //41.99682
                    Float dist= MapMath.getDistanceBetweenTwoPoints(goal[0], goal[1], marks.get(i).x , marks.get(i).y );
                  if(tempdict<dist)
                  {

                  }
                  else
                  {
                      if (dist<30){
                          tempdict=dist;
                          tempselected_index = i;
                          num=i;
                      }

                  }
                    /*if ( dist<= 50){
                        num = i;

                        isClickMark = true;
                        break;
                    }

                    if (i == marks.size() - 1) {
                        isClickMark = false;
                    }*/
                }
                if(tempselected_index!=-1)
                {
                    isClickMark=true;
                }
                else
                {
                    isClickMark=false;
                }

            }

            if (listener != null && isClickMark) {
                listener.markIsClick(num);
                mapView.refresh();
            }
        }
        }
    }

    @Override
    public void draw(Canvas canvas, Matrix currentMatrix, float currentZoom, float
            currentRotateDegrees) {
        //  source_data(String.valueOf(Pref_manager.getStoreId(my_context)));
        Typeface myCustomFont = Typeface.createFromAsset(my_context.getAssets(),"montserrat_regular.ttf");
        if (isVisible && marks != null) {
            canvas.save();
            if (!marks.isEmpty()) {
                for (int i = 0; i < marks.size(); i++) {
                    PointF mark = marks.get(i);
                    float[] goal = {mark.x, mark.y};
                    currentMatrix.mapPoints(goal);

                    paint.setColor(Color.BLACK);
                    paint.setTypeface(myCustomFont);
                    paint.setTextSize(radiusMark);
                    //yaha likha tha
//                    try {
//                        SVG svg = SVG.getFromResource(my_context,R.drawable.mapone_svg);
//                        svg.setDocumentHeight(4096f);
//                        svg.setDocumentWidth(4096f);
//                        svg.renderToCanvas(canvas);
//                    } catch (SVGParseException e) {
//                        e.printStackTrace();
//                    }
                    //mark name
                    if (mapView.getCurrentZoom() > 1.0 && marksName != null && marksName.size() == marks.size()) {


                        canvas.drawText(marksName.get(i), goal[0]-((marksName.get(i).toString().length()/2)*15), goal[1]-40, paint);
                    }
                    //mark ico
                    //if(marks.size()<10) {
                    if(!via_locate)
                        canvas.drawBitmap(bmpMark, goal[0] - bmpMark.getWidth() / 2, goal[1] - bmpMark.getHeight() / 2, paint);
                    //}
                    navigator_position_x +=lerp_float(navigator_position_x,navigator_position_target_x);
                    navigator_position_y +=lerp_float(navigator_position_y,navigator_position_target_y);
                    float[] nav_flo1 ={urhere_x,urhere_y};
                    currentMatrix.mapPoints(nav_flo1);
                   /* if(isForImHere)
                    {
                        canvas.drawBitmap(iamheremarker, nav_flo1[0] - iamheremarker.getWidth() / 2, nav_flo1[1] - iamheremarker.getHeight(), paint);
                    }*/

                    //mapView.mapCenterWithPoint(nav_flo1[0] - navigator_mark.getWidth() / 2,nav_flo1[1] - navigator_mark.getHeight());
                    //canvas.drawCircle(nav_flo[0]-navigator_mark.getWidth() / 2, nav_flo[1] - navigator_mark.getHeight()/2, 100, paint);
                    if (i == num && isClickMark) {
                        // canvas.drawBitmap(bmpMarkTouch, goal[0] - bmpMarkTouch.getWidth() / 2, goal[1] - bmpMarkTouch.getHeight(), paint);
                    }


                }

            }
            canvas.restore();
        }
    }

    private void source_data(String store_id) {

       /* List<PathNode> sourcedbNode;
        String source_beacon_id, destination_beacon_id;
        String source_store_name, destination_store_name;
        String source_floor_level, destination_floor_level;
        String destination_cordinate_x, destination_cordinate_y;
        sourcedbNode = MyApplication.Companion.get().getDb().pathNodeList().findBySourceStoreId(store_id, "Primary", "Navigational", String.valueOf(Helper.hex2decimal(store_id.substring(8,13).toString())));
        if (!sourcedbNode.isEmpty() || sourcedbNode != null || sourcedbNode.size() != 0) {
            source_beacon_id = sourcedbNode.get(0).getSite_id();
            source_store_name = sourcedbNode.get(0).getStore_name();
            urhere_x = Float.valueOf(sourcedbNode.get(0).getSite_map_coord_x());
            urhere_y = Float.valueOf(sourcedbNode.get(0).getSite_map_coord_y());
            source_floor_level = sourcedbNode.get(0).getFloor_level();



        }*/

    }

    public float lerp_float(float src,float target)
    {
        return (target-src)*.003f;
    }
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<PointF> getMarks() {
        return marks;
    }

    public void setMarks(List<PointF> marks) {
        this.marks = marks;
    }

    public List<String> getMarksName() {
        return marksName;
    }

    public void setMarksName(List<String> marksName) {
        this.marksName = marksName;
    }

    public boolean isClickMark() {
        return isClickMark;
    }

    public void setMarkIsClickListener(MarkIsClickListener listener) {
        this.listener = listener;
    }

    public interface MarkIsClickListener {
        void markIsClick(int num);
    }
}