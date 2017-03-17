package my.github.mikephil.charting.interfaces.dataprovider;

import my.github.mikephil.charting.components.YAxis;
import my.github.mikephil.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
