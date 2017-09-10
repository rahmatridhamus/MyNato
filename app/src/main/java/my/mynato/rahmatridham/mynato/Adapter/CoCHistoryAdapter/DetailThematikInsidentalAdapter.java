package my.mynato.rahmatridham.mynato.Adapter.CoCHistoryAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rahmatridham on 3/17/2017.
 */

public class DetailThematikInsidentalAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> listHeader;
    HashMap<String, List<String>> listChild;

    public DetailThematikInsidentalAdapter(Context context, List<String> listHeader, HashMap<String, List<String>> listChild) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }


    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChild.get(String.valueOf(listHeader.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChild.get(String.valueOf(listHeader.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(my.mynato.rahmatridham.mynato.R.layout.list_grouprow_doanddont, null);
        }

        TextView title = (TextView) convertView.findViewById(my.mynato.rahmatridham.mynato.R.id.titleExpandable);
        title.setTypeface(null, Typeface.NORMAL);
        title.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(my.mynato.rahmatridham.mynato.R.layout.list_subrow_doanddont, null);
        }

        TextView subtitle = (TextView) convertView.findViewById(my.mynato.rahmatridham.mynato.R.id.subtitExpandable);
        subtitle.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
