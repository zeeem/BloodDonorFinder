package finder2.donor.bllod.zeeem.blooddonorfinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DonorAdapter extends ArrayAdapter<Donor> {

    ArrayList<Donor> DonorArrayList;
    int Resource;
    Context context;

    LayoutInflater layoutInflater;


    public DonorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Donor> objects) {
        super(context, resource, objects);

        DonorArrayList = objects;
        Resource = resource;
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //creating holder for row
        ViewHolder holder;

        //getting the 1st item
        if(convertView == null){

            convertView = layoutInflater.inflate(Resource,null);
            holder = new ViewHolder();

            holder.nameTV = convertView.findViewById(R.id.nameTextV);
            holder.bloodTV = convertView.findViewById(R.id.blood_textV);
            holder.areaTV = convertView.findViewById(R.id.area_textV);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTV.setText(DonorArrayList.get(position).getName());
        holder.bloodTV.setText(DonorArrayList.get(position).getBlood());
        holder.areaTV.setText(DonorArrayList.get(position).getArea());

        return convertView;

    }


    static class ViewHolder{

        public TextView nameTV, ageTV, areaTV, bloodTV, genderTV, lonTV, latTV, historyTV;

    }
}
