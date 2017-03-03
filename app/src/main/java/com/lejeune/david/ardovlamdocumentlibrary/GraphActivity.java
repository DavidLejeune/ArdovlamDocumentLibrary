package com.lejeune.david.ardovlamdocumentlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

//import static com.lejeune.david.ardovlamdocumentlibrary.GraphActivity.restartGraph;


public class GraphActivity extends Activity {

    BarChart barChart;
    LineChart lineChart;
    //GraphView graphView;
    String filterGraphMonth;
    public int [] arrMonth;
    public int [] arrMonthHour;

    Spinner spinnerMonth;
    Spinner spinnerYear;
    Spinner spinnerType;

    ArrayAdapter<String> adapterMonth;
    ArrayAdapter<String> adapterYear;
    ArrayAdapter<String> adapterType;

    String selectedMonth , selectedYear , selectedType;
    ProgressDialog pd;

    TextView txtResultGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        txtResultGraph = (TextView) findViewById(R.id.txtResultGraph);
        txtResultGraph.setText("");
        txtResultGraph.setText("Processing ..");
        txtResultGraph.setVisibility(View.INVISIBLE);

        barChart = (BarChart) findViewById(R.id.barChart);
        barChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                System.out.println((int) e.getX());
                System.out.println((int) e.getY());
                Toast.makeText(GraphActivity.this, "Hour : " + (int) e.getX() + "\nValue : " + (int) e.getY(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNothingSelected() {

            }
        });
//
//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        barEntries.add(new BarEntry(44f , 0f));
//        barEntries.add(new BarEntry(50f , 1f));
//        barEntries.add(new BarEntry(20f , 2f));
//        barEntries.add(new BarEntry(16f , 3f));
//        barEntries.add(new BarEntry(12f , 4f));
//        barEntries.add(new BarEntry(4f , 5f));
//        IBarDataSet barDataSet = new BarDataSet(barEntries , "title");
//
//        ArrayList<String> theDays = new ArrayList<>();
//        theDays.add("1");
//        theDays.add("2");
//        theDays.add("3");
//        theDays.add("4");
//        theDays.add("5");
//        theDays.add("6");
//
//        BarData theData = new BarData(theDays,barDataSet);
//        barChart.setData(theData);
//
//        barChart.setTouchEnabled(true);


//        graphView = (GraphView) findViewById(R.id.graphView);
//
//        BarGraphSeries<DataPoint> barSeries = new BarGraphSeries<DataPoint>(new DataPoint[]{
//                //logDataPoint(),
//                new DataPoint( 1 , 4 ),
//                new DataPoint( 2 , 6 ),
//                new DataPoint( 3 , 2 ),
//                new DataPoint( 4 , 3 ),
//                new DataPoint( 5 , 4 ),
//                new DataPoint( 6 , 6 ),
//                new DataPoint( 7 , 2 ),
//                new DataPoint( 8 , 3 ),
////                MyVars.arrJune = new int[30];
////                for (int i = 0; i < MyVars.arrJune.length; i++) {
////                    new DataPoint( i + 1  , MyVars.arrJune[i])
////                }
//        });
//
//        graphView.addSeries(barSeries);
//        graphView.setTitle("Test");
//        graphView.getViewport().setMinX(0);
//        graphView.getViewport().setMaxX(32);
//        graphView.getViewport().setMinY(0.0);
//        graphView.getViewport().setMaxY(10.0);
//        graphView.getViewport().setYAxisBoundsManual(true);
//        graphView.getViewport().setXAxisBoundsManual(true);
//
//
//// styling
//        barSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//            @Override
//            public int get(DataPoint data) {
//                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getX()*255/6), 100);
//            }
//        });
//
//        barSeries.setSpacing(10);
//
//// draw values on top
//        barSeries.setDrawValuesOnTop(true);
//        barSeries.setValuesOnTopColor(Color.WHITE);

        lineChart = (LineChart) findViewById(R.id.lineChart);
        lineChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                System.out.println((int) e.getX());
                System.out.println((int) e.getY());
                Toast.makeText(GraphActivity.this, "Day : " + (int) e.getX() + "\nValue : " + (int) e.getY(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNothingSelected() {

            }
        });





        String[] months = getResources().getStringArray(R.array.monthsArray);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);

        adapterMonth = new ArrayAdapter(this, android.R.layout.simple_spinner_item, months);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = parent.getItemAtPosition(position).toString();
                MyVars.filterStatMonth = selectedMonth;
                System.out.println("selectedMonth " + selectedMonth);
//                                                      docTypeFilter = MyFilter.findVariableTypeDoc(selectedDocType);
//                                                      System.out.println("docTypeFilter " + docTypeFilter);
//                                                      //if (docTypeFilter.length()>0){
//                                                      setViewOfFilter();
//                                                      getFilteredDocuments();
//                                                      //txtResultFilter.setText(txtResultFilter.getText() +"\nType of doc filter : " + docTypeFilter);
//
// }
                switch(selectedMonth) {
                    case "January":
                        arrMonth = new int[MyVars.arrJanuary.length];
                        for (int i = 0; i < MyVars.arrJanuary.length; i++) {
                            arrMonth[i] = MyVars.arrJanuary[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrJanuaryHour.length];
                        for (int i = 0; i < MyVars.arrJanuaryHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrJanuaryHour[i] ;
                        }

                        break;
                    case "February":
                        arrMonth = new int[MyVars.arrFebruary.length];
                        for (int i = 0; i < MyVars.arrFebruary.length; i++) {
                            arrMonth[i] = MyVars.arrFebruary[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrFebruaryHour.length];
                        for (int i = 0; i < MyVars.arrFebruaryHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrFebruaryHour[i] ;
                        }
                        break;
                    case "March":
                        arrMonth = new int[MyVars.arrMarch.length];
                        for (int i = 0; i < MyVars.arrMarch.length; i++) {
                            arrMonth[i] = MyVars.arrMarch[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrMarchHour.length];
                        for (int i = 0; i < MyVars.arrMarchHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrMarchHour[i] ;
                        }
                        break;
                    case "April":
                        arrMonth = new int[MyVars.arrApril.length];
                        for (int i = 0; i < MyVars.arrApril.length; i++) {
                            arrMonth[i] = MyVars.arrApril[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrAprilHour.length];
                        for (int i = 0; i < MyVars.arrAprilHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrAprilHour[i] ;
                        }
                        break;
                    case "May":
                        arrMonth = new int[MyVars.arrMay.length];
                        for (int i = 0; i < MyVars.arrMay.length; i++) {
                            arrMonth[i] = MyVars.arrMay[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrMayHour.length];
                        for (int i = 0; i < MyVars.arrMayHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrMayHour[i] ;
                        }
                        break;
                    case "June":
                        arrMonth = new int[MyVars.arrJune.length];
                        for (int i = 0; i < MyVars.arrJune.length; i++) {
                            arrMonth[i] = MyVars.arrJune[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrJuneHour.length];
                        for (int i = 0; i < MyVars.arrJuneHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrJuneHour[i] ;
                        }
                        break;
                    case "July":
                        arrMonth = new int[MyVars.arrJuly.length];
                        for (int i = 0; i < MyVars.arrJuly.length; i++) {
                            arrMonth[i] = MyVars.arrJuly[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrJulyHour.length];
                        for (int i = 0; i < MyVars.arrJulyHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrJulyHour[i] ;
                        }
                        break;
                    case "Augustus":
                        arrMonth = new int[MyVars.arrAugustus.length];
                        for (int i = 0; i < MyVars.arrAugustus.length; i++) {
                            arrMonth[i] = MyVars.arrAugustus[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrAugustusHour.length];
                        for (int i = 0; i < MyVars.arrAugustusHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrAugustusHour[i] ;
                        }
                        break;
                    case "September":
                        arrMonth = new int[MyVars.arrSeptember.length];
                        for (int i = 0; i < MyVars.arrSeptember.length; i++) {
                            arrMonth[i] = MyVars.arrSeptember[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrSeptemberHour.length];
                        for (int i = 0; i < MyVars.arrSeptemberHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrSeptemberHour[i] ;
                        }
                        break;
                    case "October":
                        arrMonth = new int[MyVars.arrOctober.length];
                        for (int i = 0; i < MyVars.arrOctober.length; i++) {
                            arrMonth[i] = MyVars.arrOctober[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrOctoberHour.length];
                        for (int i = 0; i < MyVars.arrOctoberHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrOctoberHour[i] ;
                        }
                        break;
                    case "November":
                        arrMonth = new int[MyVars.arrNovember.length];
                        for (int i = 0; i < MyVars.arrNovember.length; i++) {
                            arrMonth[i] = MyVars.arrNovember[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrNovemberHour.length];
                        for (int i = 0; i < MyVars.arrNovemberHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrNovemberHour[i] ;
                        }
                        break;
                    case "December":
                        arrMonth = new int[MyVars.arrDecember.length];
                        for (int i = 0; i < MyVars.arrDecember.length; i++) {
                            arrMonth[i] = MyVars.arrDecember[i] ;
                        }
                        arrMonthHour = new int[MyVars.arrDecemberHour.length];
                        for (int i = 0; i < MyVars.arrDecemberHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrDecemberHour[i] ;
                        }
                        break;
                    case "":
                        System.out.println("nothing");
                        int lengthYear = MyVars.arrJanuary.length + MyVars.arrFebruary.length + MyVars.arrMarch.length +
                                MyVars.arrApril.length + MyVars.arrMay.length + MyVars.arrJune.length +
                                MyVars.arrJuly.length + MyVars.arrAugustus.length + MyVars.arrSeptember.length +
                                MyVars.arrOctober.length + MyVars.arrNovember.length + MyVars.arrDecember.length ;
                        arrMonth = new int[lengthYear];
                        System.out.println("lengthYear " + lengthYear);
//                          System.arraycopy(MyVars.arrJanuary, 0, arrMonth, 0, MyVars.arrJanuary.length);
//                          System.arraycopy(MyVars.arrFebruary, 0, arrMonth, MyVars.arrJanuary.length, MyVars.arrFebruary.length);
//                          System.arraycopy(MyVars.arrMarch, 0, arrMonth, MyVars.arrFebruary.length, MyVars.arrMarch.length);
//                          System.arraycopy(MyVars.arrApril, 0, arrMonth, MyVars.arrMarch.length, MyVars.arrApril.length);
//                          System.arraycopy(MyVars.arrMay, 0, arrMonth, MyVars.arrApril.length, MyVars.arrMay.length);
//                          System.arraycopy(MyVars.arrJune, 0, arrMonth, MyVars.arrMay.length, MyVars.arrJune.length);
//                          System.arraycopy(MyVars.arrJuly, 0, arrMonth, MyVars.arrJune.length, MyVars.arrJuly.length);
//                          System.arraycopy(MyVars.arrAugustus, 0, arrMonth, MyVars.arrJuly.length, MyVars.arrAugustus.length);
//                          System.arraycopy(MyVars.arrSeptember, 0, arrMonth, MyVars.arrAugustus.length, MyVars.arrSeptember.length);
//                          System.arraycopy(MyVars.arrOctober, 0, arrMonth, MyVars.arrSeptember.length, MyVars.arrOctober.length);
//                          System.arraycopy(MyVars.arrNovember, 0, arrMonth, MyVars.arrOctober.length, MyVars.arrNovember.length);
//                          System.arraycopy(MyVars.arrDecember, 0, arrMonth, MyVars.arrNovember.length, MyVars.arrDecember.length);
//                          for (int i = 0; i < MyVars.arrFebruary.length; i++) {
//                              System.out.println(MyVars.arrFebruary[i]);

                        arrMonth = merge(MyVars.arrJanuary ,MyVars.arrFebruary, MyVars.arrMarch , MyVars.arrApril, MyVars.arrMay,MyVars.arrJune, MyVars.arrJuly,MyVars.arrAugustus,MyVars.arrSeptember, MyVars.arrOctober, MyVars.arrNovember, MyVars.arrDecember);
//
                        for ( int x: arrMonth )
                            System.out.print( " x : " + x );

//                          }


                        arrMonthHour = new int[24];
                        for (int i = 0; i < arrMonthHour.length; i++) {
                            arrMonthHour[i] = MyVars.arrJanuaryHour[i] + MyVars.arrFebruaryHour[i] + MyVars.arrMarchHour[i] + MyVars.arrAprilHour[i] + MyVars.arrMayHour[i] + MyVars.arrJuneHour[i] + MyVars.arrJulyHour[i] + MyVars.arrAugustusHour[i] + MyVars.arrSeptemberHour[i] + MyVars.arrOctoberHour[i] +  MyVars.arrNovemberHour[i] + MyVars.arrDecemberHour[i] ;
                            //System.out.println("combine arrayvalue: " + arrMonthHour[i]);
                        }



                        //MyVars.filterStatType = "Full year";
                        break;
                }
//                  if (!selectedMonth.equalsIgnoreCase(""))
//                  {
                createBarGraph();
                createLineChart();

//                  }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //set spinner to current year
        //the value you want the position for
        ArrayAdapter myAdapMonth = (ArrayAdapter) spinnerMonth.getAdapter(); //cast to an ArrayAdapter
        int spinnerPositionMonth = myAdapMonth.getPosition(MyVars.filterStatMonth);
        //set the default according to value
        spinnerMonth.setSelection(spinnerPositionMonth);







        String[] years = getResources().getStringArray(R.array.yearsArray);
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);

        adapterYear = new ArrayAdapter(this, android.R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerYear.setAdapter(adapterYear);

        //set spinner to current year
        //the value you want the position for
        ArrayAdapter myAdap = (ArrayAdapter) spinnerYear.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(MyVars.filterStatYear);
        //set the default according to value
        spinnerYear.setSelection(spinnerPosition);

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
                System.out.println("selectedYear " + selectedYear);
                if(selectedYear.equalsIgnoreCase(MyVars.filterStatYear)){

                }
                else
                {
                    txtResultGraph.setVisibility(View.VISIBLE);
                    MyVars.filterStatYear = selectedYear;
                    new AsyncCreateStatsDL().execute();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        String[] types = getResources().getStringArray(R.array.logTypes);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);

        adapterType = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapterType);

        //set spinner to current year
        //the value you want the position for
        ArrayAdapter myAdapType = (ArrayAdapter) spinnerType.getAdapter(); //cast to an ArrayAdapter
        int spinnerPositionType = myAdapType.getPosition(MyVars.filterStatType);

        //set the default according to value
        spinnerType.setSelection(spinnerPositionType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
                System.out.println("selectedType " + selectedType);
                if(selectedType.equalsIgnoreCase(MyVars.filterStatType)){

                }
                else
                {

                    txtResultGraph.setVisibility(View.VISIBLE);
                    MyVars.filterStatType = selectedType;
                    new AsyncCreateStatsDL().execute();
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        System.out.println("type : " + MyVars.filterStatType);
        System.out.println("year : " + MyVars.filterStatYear);
        System.out.println("month : " + MyVars.filterStatMonth);



    }



    public int[] merge(final int[] ...arrays ) {
        int size = 0;
        for ( int[] a: arrays )
            size += a.length;

        int[] res = new int[size];

        int destPos = 0;
        for ( int i = 0; i < arrays.length; i++ ) {
            if ( i > 0 ) destPos += arrays[i-1].length;
            int length = arrays[i].length;
            System.arraycopy(arrays[i], 0, res, destPos, length);
        }

        return res;
    }

    private void createLineChart(){
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < arrMonth.length; i++) {
            float iFloat = (float) i + 1;
            float arrFloat = (float) arrMonth[i];
            //System.out.println(arrFloat + " " + arrMonth.length);

            Entry entry = new Entry(iFloat, arrFloat); // 0 == quarter 1
            entries.add(entry);

        }
        LineDataSet lineDataSet = new LineDataSet(entries, MyVars.filterStatType + " / Day");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        data.setDrawValues(false);

        //styling
        lineChart.getAxisRight().setEnabled(false);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0);

        XAxis xl = lineChart.getXAxis();
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yl = lineChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);

        lineChart.getAxisLeft().setTextColor(Color.YELLOW); // left y-axis
        lineChart.getXAxis().setTextColor(Color.WHITE);
        lineChart.getLegend().setTextColor(Color.BLUE);
        lineChart.getDescription().setTextColor(Color.WHITE);
        lineChart.getLineData().setValueTextColor(Color.MAGENTA);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        lineChart.invalidate(); // refresh

    }


    private void createBarGraph(){

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < arrMonthHour.length; i++) {
            //System.out.println("arrMonthHour.length : " + arrMonthHour.length);
            float iFloat = (float) i;
            float arrFloat = (float) arrMonthHour[i];
            //System.out.println(iFloat + " " + arrFloat);
            entries.add(new BarEntry(iFloat, arrFloat));
        }

//        entries.add(new BarEntry(0f, 30f));
//        entries.add(new BarEntry(1f, 80f));
//        entries.add(new BarEntry(2f, 60f));
//        entries.add(new BarEntry(3f, 50f));
//        // gap of 2f
//        entries.add(new BarEntry(5f, 70f));
//        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, MyVars.filterStatType + " / Hour");
        set.setDrawValues(false);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        float arrLength = (float) arrMonthHour.length;

        xAxis.setAxisMaximum(arrLength + 1);
        //xAxis.setGranularity(5f);


        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);

//        barChart.getAxisLeft().setDrawGridLines(false);
//        barChart.getAxisLeft().setDrawGridLines(false);
//        barChart.getAxisRight().setDrawGridLines(false);
//        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);



        XAxis xl = barChart.getXAxis();
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
//
        YAxis yl = barChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
//
//        YAxis yr = barChart.getAxisRight();
//        yr.setDrawAxisLine(true);
//        yr.setDrawGridLines(false);


        //yAxis.setLabelCount(10, true);
//        yAxis.setGranularityEnabled(true);
//        yAxis.setGranularity(10f);

        barChart.getAxisLeft().setTextColor(Color.YELLOW); // left y-axis
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getLegend().setTextColor(Color.BLUE);
        barChart.getDescription().setTextColor(Color.WHITE);
        barChart.getBarData().setValueTextColor(Color.MAGENTA);

        barChart.invalidate(); // refresh
    }

    public void restartGraph(){

        finish();
        startActivity(getIntent());
    }





    public class AsyncCreateStatsDL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MyStats.createMonthArrays();
            MyStats.createMonthHourArrays();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            restartGraph();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            MyStats.filterBigStatFiles(MyVars.filterStatYear, MyVars.filterStatType);

            return null;
        }






    }




//    private void logDataPoint(){
//                for (int i = 0; i < MyVars.arrJune.length; i++) {
//                    new DataPoint( i + 1  , MyVars.arrJune[i])
//                }
//    }
}

