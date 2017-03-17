package my.github.mikephil.charting.interfaces.dataprovider;

import my.github.mikephil.charting.components.YAxis.AxisDependency;
import my.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import my.github.mikephil.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
