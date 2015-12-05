package assign3.weather.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import assign3.weather.R;
import assign3.weather.customViews.DejaVUSansTextView;
import assign3.weather.customViews.RobotoTextView;
import assign3.weather.objects.ListItemObject;

import static assign3.weather.R.drawable.w01d;

/**
 * Created by dalkh on 05-Dec-15.
 */
@SuppressWarnings("ResourceType")
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<ListItemObject> forecastList=new ArrayList<>();
    Context context;
    public  ForecastRecyclerViewAdapter(){

    }
    public ForecastRecyclerViewAdapter(ArrayList<ListItemObject> forecastList,Context context){
        this.context=context;
        this.forecastList=forecastList;
    }
    @Override
    public ForecastRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        MyViewHolder viewHolder= new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerViewAdapter.MyViewHolder holder, int position) {
              holder.dateView.setText(forecastList.get(position).getDate());
             holder.weekDayView.setText(forecastList.get(position).getWeekDay());
        Drawable dr=this.context.getResources().getDrawable(this.context.getResources().getIdentifier("sw"+forecastList.get(position).getIcon(), "drawable", this.context.getPackageName()));
            holder.iconView.setImageDrawable(dr);
        holder.tempMaxView.setText(forecastList.get(position).getTempMax()+"º/");
        holder.tempMinView.setText(forecastList.get(position).getTempMin()+"º");
    }

    @Override
    public int getItemCount() {
        return forecastList==null ? 0: forecastList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        RobotoTextView dateView,tempMaxView,tempMinView;
        DejaVUSansTextView weekDayView;
        ImageView iconView;
               public MyViewHolder(View itemView){
                   super(itemView);
                   dateView= (RobotoTextView) itemView.findViewById(R.id.date);
                   weekDayView= (DejaVUSansTextView) itemView.findViewById(R.id.weekDay);
                   iconView= (ImageView) itemView.findViewById(R.id.icon);
                   tempMaxView= (RobotoTextView) itemView.findViewById(R.id.temp_max);
                   tempMinView= (RobotoTextView) itemView.findViewById(R.id.temp_min);

               }
    }
}
